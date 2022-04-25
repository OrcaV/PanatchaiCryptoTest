package com.v.panatchai.cryptocurrency.domain.coins

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.google.common.truth.Truth.assertThat
import com.v.panatchai.cryptocurrency.data.repositories.coins.CoinFactory
import com.v.panatchai.cryptocurrency.data.repositories.coins.ICoinRepository
import com.v.panatchai.cryptocurrency.domain.UseCase
import com.v.panatchai.cryptocurrency.domain.models.coins.Coin
import com.v.panatchai.cryptocurrency.enums.OrderBy
import com.v.panatchai.cryptocurrency.mappers.map
import com.v.panatchai.cryptocurrency.utils.NoopListCallback
import com.v.panatchai.cryptocurrency.utils.TestDiffCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class CurrencyListUseCaseTest {

    private val testScope = TestScope()
    private val testDispatcher = StandardTestDispatcher(testScope.testScheduler)
    private val coinRepo: ICoinRepository = mock()

    private lateinit var useCase: UseCase<CurrencyArgument, Flow<PagingData<Coin>>>

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        useCase = CurrencyListUseCase(coinRepo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `WhenInvoke ReturnListOfCoins`() = testScope.runTest {
        // init mocks
        val entity1 = CoinFactory.create("name1", "symbol1")
        val entity2 = CoinFactory.create("name2", "symbol2")
        val entity3 = CoinFactory.create("name3", "symbol3")
        val differ = AsyncPagingDataDiffer<Coin>(
            diffCallback = TestDiffCallback<Coin>(),
            updateCallback = NoopListCallback(),
            workerDispatcher = Dispatchers.Main
        )

        val testFlow = flow {
            emit(PagingData.from(listOf(entity1, entity2, entity3)))
        }
        whenever(coinRepo.fetchCoins(any(), any())).thenReturn(testFlow)

        // execute test
        val arg = CurrencyArgument(OrderBy.ASC, "")
        val actualFlow = useCase(arg)

        // verify
        val job = launch {
            actualFlow.collectLatest {
                differ.submitData(it)
            }
        }

        advanceUntilIdle()
        assertThat(differ.snapshot().items).containsExactly(
            entity1.map(),
            entity2.map(),
            entity3.map()
        )
        verify(coinRepo).fetchCoins(OrderBy.ASC, "")

        // clean up
        job.cancel()
    }

    @Test
    fun `GivenNoArgument WhenInvoke Throw IllegalArgumentException`() = testScope.runTest {
        // execute test
        val e = assertThrows(IllegalArgumentException::class.java) {
            runBlocking {
                useCase()
            }
        }

        // verify
        assertThat(e).isInstanceOf(IllegalArgumentException::class.java)
        assertThat(e).hasMessageThat().isEqualTo("Invalid Currency Argument")
        verify(coinRepo, never()).fetchCoins(any(), any())
    }
}
