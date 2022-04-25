package com.v.panatchai.cryptocurrency.presentation.coins

import androidx.lifecycle.SavedStateHandle
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.map
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.v.panatchai.cryptocurrency.data.repositories.coins.CoinFactory
import com.v.panatchai.cryptocurrency.domain.UseCase
import com.v.panatchai.cryptocurrency.domain.coins.CurrencyArgument
import com.v.panatchai.cryptocurrency.domain.models.coins.Coin
import com.v.panatchai.cryptocurrency.enums.OrderBy
import com.v.panatchai.cryptocurrency.mappers.map
import com.v.panatchai.cryptocurrency.presentation.coins.views.CurrencyViewModel
import com.v.panatchai.cryptocurrency.presentation.models.CurrencyModel
import com.v.panatchai.cryptocurrency.presentation.models.ListSeparator
import com.v.panatchai.cryptocurrency.presentation.models.UiModel
import com.v.panatchai.cryptocurrency.utils.NoopListCallback
import com.v.panatchai.cryptocurrency.utils.TestDiffCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
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
import org.robolectric.annotation.Config

@OptIn(ExperimentalPagingApi::class, ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
@Config(maxSdk = 30)
class CurrencyViewModelTest {

    private val testScope = TestScope()
    private val testDispatcher = StandardTestDispatcher(testScope.testScheduler)

    private val bitcoin = CoinFactory.create("Bitcoin", "BTC").map()
    private val ethereum = CoinFactory.create("Ethereum", "ETH").map()

    private lateinit var currencyList: UseCase<CurrencyArgument, Flow<PagingData<Coin>>>
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var vm: CurrencyViewModel

    @Before
    fun init() = runBlocking {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GivenListOfCoins WhenSubmit Emit UiModel with ListSeparator`() = runTest {
        // init
        val actualList = mutableListOf<UiModel>()
        val signalFlow = MutableStateFlow(0)
        val consumerFlow = MutableStateFlow<UiModel>(ListSeparator(""))
        val differ = AsyncPagingDataDiffer<UiModel>(
            diffCallback = TestDiffCallback(),
            updateCallback = NoopListCallback(),
            workerDispatcher = Dispatchers.Main
        )
        savedStateHandle = SavedStateHandle()
        currencyList = object : UseCase<CurrencyArgument, Flow<PagingData<Coin>>>() {
            override suspend fun invoke(vararg args: CurrencyArgument) = flow {
                val pagingData = PagingData.from(listOf(bitcoin, ethereum))
                emit(pagingData)
            }
        }
        vm = CurrencyViewModel(currencyList, savedStateHandle)

        // execute test
        val producerJob = launch {
            signalFlow.combine(vm.currencies) { _, page ->
                page.map { model ->
                    actualList.add(model)
                    consumerFlow.emit(model)
                    model
                }
            }.collectLatest {
                differ.submitData(it)
            }
        }

        // verify
        val consumerJob = launch {
            consumerFlow.collectLatest {
                if (it is CurrencyModel && it.name == "Ethereum") {
                    assertThat(actualList).containsExactly(
                        bitcoin.map(),
                        ListSeparator("${bitcoin.name}-${ethereum.name}"),
                        ethereum.map()
                    )
                    this@launch.cancel()
                }
            }
        }

        // clean up
        consumerJob.join()
        producerJob.cancel()
    }

    @Test
    fun `GivenFilter Then Pass Filter onto UseCase`() = runTest {
        // init
        val actualList = mutableListOf<UiModel>()
        val signalFlow = MutableStateFlow(0)
        val consumerFlow = MutableStateFlow<UiModel>(ListSeparator(""))
        val differ = AsyncPagingDataDiffer<UiModel>(
            diffCallback = TestDiffCallback(),
            updateCallback = NoopListCallback(),
            workerDispatcher = Dispatchers.Main
        )
        var actualFilter = ""
        var actualOrderBy = OrderBy.DESC
        savedStateHandle = SavedStateHandle()
        currencyList = object : UseCase<CurrencyArgument, Flow<PagingData<Coin>>>() {
            override suspend fun invoke(vararg args: CurrencyArgument) = flow {
                actualFilter = args[0].filter
                actualOrderBy = args[0].orderBy
                val pagingData = PagingData.from(listOf(bitcoin))
                emit(pagingData)
            }
        }
        vm = CurrencyViewModel(currencyList, savedStateHandle)

        // execute test
        vm.filter("B")
        val producerJob = launch {
            signalFlow.combine(vm.currencies) { _, page ->
                page.map { model ->
                    println("- $model")
                    actualList.add(model)
                    consumerFlow.emit(model)
                    model
                }
            }.collectLatest {
                differ.submitData(it)
            }
        }

        // verify
        val consumerJob = launch {
            consumerFlow.collectLatest {
                if (it is CurrencyModel && it.name == "Bitcoin") {
                    assertThat(actualList).containsExactly(
                        bitcoin.map()
                    )
                    assertThat(actualFilter).isEqualTo("B")
                    assertThat(actualOrderBy).isEqualTo(OrderBy.ASC)
                    this@launch.cancel()
                }
            }
        }

        // clean up
        consumerJob.join()
        producerJob.cancel()
    }

    @Test
    fun `GivenOrderByDesc Then Pass DESC onto UseCase`() = runTest {
        // init
        val actualList = mutableListOf<UiModel>()
        val signalFlow = MutableStateFlow(0)
        val consumerFlow = MutableStateFlow<UiModel>(ListSeparator(""))
        val differ = AsyncPagingDataDiffer<UiModel>(
            diffCallback = TestDiffCallback(),
            updateCallback = NoopListCallback(),
            workerDispatcher = Dispatchers.Main
        )
        var actualOrderBy = OrderBy.ASC
        savedStateHandle = SavedStateHandle()
        currencyList = object : UseCase<CurrencyArgument, Flow<PagingData<Coin>>>() {
            override suspend fun invoke(vararg args: CurrencyArgument) = flow {
                actualOrderBy = args[0].orderBy
                val pagingData = PagingData.from(listOf(ethereum))
                emit(pagingData)
            }
        }
        vm = CurrencyViewModel(currencyList, savedStateHandle)

        // execute test
        vm.toggleOrderBy()
        val producerJob = launch {
            signalFlow.combine(vm.currencies) { _, page ->
                page.map { model ->
                    println("- $model")
                    actualList.add(model)
                    consumerFlow.emit(model)
                    model
                }
            }.collectLatest {
                differ.submitData(it)
            }
        }

        // verify
        val consumerJob = launch {
            consumerFlow.collectLatest {
                if (it is CurrencyModel && it.name == "Ethereum") {
                    assertThat(actualList).containsExactly(
                        ethereum.map()
                    )
                    assertThat(actualOrderBy).isEqualTo(OrderBy.DESC)
                    this@launch.cancel()
                }
            }
        }

        // clean up
        consumerJob.join()
        producerJob.cancel()
    }

    @Test
    fun `WhenToggleOrderBy ToggleOrderBy`() = runTest {
        // init
        savedStateHandle = SavedStateHandle()
        currencyList = object : UseCase<CurrencyArgument, Flow<PagingData<Coin>>>() {
            override suspend fun invoke(vararg args: CurrencyArgument) = flow {
                val pagingData = PagingData.from(listOf(ethereum))
                emit(pagingData)
            }
        }
        vm = CurrencyViewModel(currencyList, savedStateHandle)

        // pre-condition
        assertThat(vm.orderBy.value).isEqualTo(OrderBy.ASC)

        // execute test
        vm.toggleOrderBy()

        // verify
        assertThat(vm.orderBy.value).isEqualTo(OrderBy.DESC)

        // execute test
        vm.toggleOrderBy()

        // verify
        assertThat(vm.orderBy.value).isEqualTo(OrderBy.ASC)
    }
}
