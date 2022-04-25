package com.v.panatchai.cryptocurrency.data.mediators

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.paging.RemoteMediator.MediatorResult
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.v.panatchai.cryptocurrency.data.database.CoinDatabase
import com.v.panatchai.cryptocurrency.data.models.coins.CoinEntity
import com.v.panatchai.cryptocurrency.data.models.coins.CoinResponse
import com.v.panatchai.cryptocurrency.data.repositories.coins.CoinFactory
import com.v.panatchai.cryptocurrency.data.services.CoinService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.robolectric.annotation.Config

@OptIn(ExperimentalPagingApi::class, ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
@Config(maxSdk = 30)
class CoinServiceMediatorTest {

    private val coinService: CoinService = mock()
    private val bitcoin = CoinFactory.create("Bitcoin", "BTC")
    private val ethereum = CoinFactory.create("Ethereum", "ETH")
    private val iot = CoinFactory.create("Iot", "IoT")

    private lateinit var db: CoinDatabase
    private lateinit var remoteMediator: RemoteMediator<Int, CoinEntity>

    @Before
    fun init() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, CoinDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        remoteMediator = CoinServiceMediator(db, coinService, 2)
    }

    @Test
    fun `GivenRefreshAndMoreDataIsPresent WhenLoad ReturnsSuccessResult + MorePages`() = runTest {
        // init
        val pagingState = PagingState<Int, CoinEntity>(
            listOf(),
            null,
            PagingConfig(2),
            0
        )
        whenever(coinService.getCoinList(any(), any())).thenReturn(
            CoinResponse(listOf(bitcoin, ethereum))
        )

        // execute test
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)

        // verify
        assertThat(result).isInstanceOf(MediatorResult.Success::class.java) // ReturnsSuccessResult
        assertThat((result as MediatorResult.Success).endOfPaginationReached).isFalse() // MorePages
    }

    @Test
    fun `GivenRefreshAndMoreDataIsNotPresent WhenLoad ReturnsSuccessResult + EndOfPage`() =
        runTest {
            // init
            val pagingState = PagingState<Int, CoinEntity>(
                listOf(),
                null,
                PagingConfig(2),
                0
            )
            whenever(coinService.getCoinList(any(), any())).thenReturn(
                CoinResponse(listOf(bitcoin))
            )

            // execute test
            val result = remoteMediator.load(LoadType.REFRESH, pagingState)

            // verify
            assertThat(result).isInstanceOf(MediatorResult.Success::class.java) // ReturnsSuccessResult
            assertThat((result as MediatorResult.Success).endOfPaginationReached).isTrue() // EndOfPage
        }

    @Test
    fun `GivenRefreshAndError WhenLoad ReturnsError`() =
        runTest {
            // init
            val pagingState = PagingState<Int, CoinEntity>(
                listOf(),
                null,
                PagingConfig(2),
                0
            )
            doThrow(RuntimeException("error")).whenever(coinService).getCoinList(any(), any())

            // execute test
            val result = remoteMediator.load(LoadType.REFRESH, pagingState)

            // verify
            assertThat(result).isInstanceOf(MediatorResult.Error::class.java) // ReturnsSuccessResult
        }

    @Test
    fun `GivenNoLocalData WhenInitialize Return LAUNCH_INITIAL_REFRESH`() = runTest {
        // init
        db.coinDao().clear()

        // execute test
        val action = remoteMediator.initialize()

        // verify
        assertThat(action).isEqualTo(RemoteMediator.InitializeAction.LAUNCH_INITIAL_REFRESH)
    }

    @Test
    fun `GivenLocalData WhenInitialize Return SKIP_INITIAL_REFRESH`() = runTest {
        // init
        db.coinDao().insert(listOf(bitcoin, ethereum))

        // execute test
        val action = remoteMediator.initialize()

        // verify
        assertThat(action).isEqualTo(RemoteMediator.InitializeAction.SKIP_INITIAL_REFRESH)
    }

    @Test
    fun `GivenPrepend WhenLoad ReturnSuccess + EndOfPage`() = runTest {
        // init
        val pagingState = PagingState<Int, CoinEntity>(
            listOf(),
            null,
            PagingConfig(2),
            0
        )

        // execute test
        val result = remoteMediator.load(LoadType.PREPEND, pagingState)

        // verify
        assertThat(result).isInstanceOf(MediatorResult.Success::class.java) // ReturnsSuccessResult
        assertThat((result as MediatorResult.Success).endOfPaginationReached).isTrue() // EndOfPage
        verify(coinService, never()).getCoinList(any(), any())
    }

    @Test
    fun `GivenAppendAndEndOfPage WhenLoad ReturnSuccess + EndOfPage`() = runTest {
        // init
        whenever(coinService.getCoinList(any(), any())).thenReturn(
            CoinResponse(listOf(iot))
        )

        // execute test
        val pageState = PagingState<Int, CoinEntity>(
            listOf(),
            null,
            PagingConfig(2),
            0
        )
        val newResult = remoteMediator.load(LoadType.APPEND, pageState)

        // verify
        assertThat(newResult).isInstanceOf(MediatorResult.Success::class.java) // ReturnsSuccessResult
        assertThat((newResult as MediatorResult.Success).endOfPaginationReached).isTrue() // EndOfPage

        val indexCaptor = argumentCaptor<Int>()
        val pageSizeCaptor = argumentCaptor<Int>()
        verify(coinService).getCoinList(indexCaptor.capture(), pageSizeCaptor.capture())
        assertThat(indexCaptor.firstValue).isEqualTo(0)
        assertThat(pageSizeCaptor.firstValue).isEqualTo(2)
    }

    @After
    fun tearDown() {
        db.clearAllTables()
        db.close()
    }
}
