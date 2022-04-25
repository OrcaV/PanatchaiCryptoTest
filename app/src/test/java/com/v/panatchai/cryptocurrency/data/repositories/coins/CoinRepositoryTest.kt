package com.v.panatchai.cryptocurrency.data.repositories.coins

import android.content.Context
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.ExperimentalPagingApi
import androidx.paging.RemoteMediator
import androidx.paging.map
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.v.panatchai.cryptocurrency.data.database.CoinDatabase
import com.v.panatchai.cryptocurrency.data.mediators.CoinServiceMediator
import com.v.panatchai.cryptocurrency.data.models.coins.CoinEntity
import com.v.panatchai.cryptocurrency.data.models.coins.CoinResponse
import com.v.panatchai.cryptocurrency.data.services.CoinService
import com.v.panatchai.cryptocurrency.enums.OrderBy
import com.v.panatchai.cryptocurrency.utils.NoopListCallback
import com.v.panatchai.cryptocurrency.utils.TestDiffCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.robolectric.annotation.Config

@OptIn(ExperimentalPagingApi::class, ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
@Config(maxSdk = 30)
class CoinRepositoryTest {

    private val testScope = TestScope()
    private val testDispatcher = StandardTestDispatcher(testScope.testScheduler)
    private val coinService: CoinService = mock()
    private val bitcoin = CoinFactory.create("Bitcoin", "BTC")
    private val ethereum = CoinFactory.create("Ethereum", "ETH")

    private lateinit var remoteMediator: RemoteMediator<Int, CoinEntity>
    private lateinit var repo: ICoinRepository
    private lateinit var db: CoinDatabase

    @Before
    fun init() = runBlocking {
        Dispatchers.setMain(testDispatcher)

        whenever(coinService.getCoinList(any(), any())).thenReturn(
            CoinResponse(listOf(bitcoin, ethereum))
        )

        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, CoinDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        remoteMediator = CoinServiceMediator(db, coinService, 2)

        repo = CoinRepository(db, remoteMediator, 2, 2)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()

        db.clearAllTables()
        db.close()
    }

    @Test
    fun `WhenFetchCoins FetchDatabaseFirstThenNetwork`() = testScope.runTest {
        // init mocks
        val differ = AsyncPagingDataDiffer<CoinEntity>(
            diffCallback = TestDiffCallback(),
            updateCallback = NoopListCallback(),
            workerDispatcher = Dispatchers.Main
        )

        // pre-condition
        assertThat(db.coinDao().selectAll()).isEmpty()

        // execute test
        var i = 0
        val signalFlow = MutableStateFlow(i)
        val producerJob = launch {
            repo.fetchCoins(OrderBy.ASC, "")
                .collectLatest {
                    signalFlow.emit(++i)
                    differ.submitData(it)
                }
        }

        // verify
        val consumerJob = launch {
            signalFlow.collectLatest {
                if (it > 1) {
                    assertThat(db.coinDao().selectAll()).containsExactly(
                        bitcoin, ethereum
                    )
                    verify(coinService).getCoinList(any(), any())
                    this@launch.cancel()
                }
            }
        }

        // clean up
        consumerJob.join()
        producerJob.cancel()
    }

    @Test
    fun `GivenNoFilterAsc WhenFetchCoins ReturnAllAsc`() = testScope.runTest {
        // init mocks
        val differ = AsyncPagingDataDiffer<CoinEntity>(
            diffCallback = TestDiffCallback(),
            updateCallback = NoopListCallback(),
            workerDispatcher = Dispatchers.Main
        )

        // execute test
        val signalFlow = MutableStateFlow(0)
        val consumerFlow = MutableStateFlow(CoinFactory.create("", ""))
        val actualList = mutableListOf<CoinEntity>()
        val producerJob = launch {
            signalFlow.combine(repo.fetchCoins(OrderBy.ASC, "")) { _, c ->
                c.map {
                    actualList.add(it)
                    consumerFlow.emit(it)
                    it
                }
            }.collectLatest {
                differ.submitData(it)
            }
        }

        // verify
        val expectedList = listOf(bitcoin, ethereum)
        val consumerJob = launch {
            consumerFlow.collectLatest {
                if (it.name == "Ethereum") {
                    assertThat(actualList).isEqualTo(expectedList)
                    this@launch.cancel()
                }
            }
        }

        // clean up
        consumerJob.join()
        producerJob.cancel()
    }

    @Test
    fun `GivenNoFilterDesc WhenFetchCoins ReturnAllDesc`() = testScope.runTest {
        // init mocks
        val differ = AsyncPagingDataDiffer<CoinEntity>(
            diffCallback = TestDiffCallback(),
            updateCallback = NoopListCallback(),
            workerDispatcher = Dispatchers.Main
        )

        // execute test
        val signalFlow = MutableStateFlow(0)
        val consumerFlow = MutableStateFlow(CoinFactory.create("", ""))
        val actualList = mutableListOf<CoinEntity>()
        val producerJob = launch {
            signalFlow.combine(repo.fetchCoins(OrderBy.DESC, "")) { _, c ->
                c.map {
                    actualList.add(it)
                    consumerFlow.emit(it)
                    it
                }
            }.collectLatest {
                differ.submitData(it)
            }
        }

        // verify
        val expectedList = listOf(ethereum, bitcoin)
        val consumerJob = launch {
            consumerFlow.collectLatest {
                if (it.name == "Bitcoin") {
                    assertThat(actualList).isEqualTo(expectedList)
                    this@launch.cancel()
                }
            }
        }

        // clean up
        consumerJob.join()
        producerJob.cancel()
    }

    @Test
    fun `GivenFilterAsc WhenFetchCoins ReturnFilteredListAsc`() = testScope.runTest {
        // init mocks
        val differ = AsyncPagingDataDiffer<CoinEntity>(
            diffCallback = TestDiffCallback(),
            updateCallback = NoopListCallback(),
            workerDispatcher = Dispatchers.Main
        )

        // execute test
        val signalFlow = MutableStateFlow(0)
        val consumerFlow = MutableStateFlow(CoinFactory.create("", ""))
        val actualList = mutableListOf<CoinEntity>()
        val producerJob = launch {
            signalFlow.combine(repo.fetchCoins(OrderBy.ASC, "E")) { _, c ->
                c.map {
                    actualList.add(it)
                    consumerFlow.emit(it)
                    it
                }
            }.collectLatest {
                differ.submitData(it)
            }
        }

        // verify
        val expectedList = listOf(ethereum)
        val consumerJob = launch {
            consumerFlow.collectLatest {
                if (it.name == "Ethereum") {
                    assertThat(actualList).isEqualTo(expectedList)
                    this@launch.cancel()
                }
            }
        }

        // clean up
        consumerJob.join()
        producerJob.cancel()
    }

    @Test
    fun `GivenFilterDesc WhenFetchCoins ReturnFilteredListDesc`() = testScope.runTest {
        // init mocks
        val differ = AsyncPagingDataDiffer<CoinEntity>(
            diffCallback = TestDiffCallback(),
            updateCallback = NoopListCallback(),
            workerDispatcher = Dispatchers.Main
        )

        // execute test
        val signalFlow = MutableStateFlow(0)
        val consumerFlow = MutableStateFlow(CoinFactory.create("", ""))
        val actualList = mutableListOf<CoinEntity>()
        val producerJob = launch {
            signalFlow.combine(repo.fetchCoins(OrderBy.DESC, "B")) { _, c ->
                c.map {
                    actualList.add(it)
                    consumerFlow.emit(it)
                    it
                }
            }.collectLatest {
                differ.submitData(it)
            }
        }

        // verify
        val expectedList = listOf(bitcoin)
        val consumerJob = launch {
            consumerFlow.collectLatest {
                if (it.name == "Bitcoin") {
                    assertThat(actualList).isEqualTo(expectedList)
                    this@launch.cancel()
                }
            }
        }

        // clean up
        consumerJob.join()
        producerJob.cancel()
    }
}
