package com.v.panatchai.cryptocurrency.data.dao

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadResult.Page
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.v.panatchai.cryptocurrency.data.database.CoinDatabase
import com.v.panatchai.cryptocurrency.data.repositories.coins.CoinFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@OptIn(ExperimentalPagingApi::class, ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
@Config(maxSdk = 30)
class CoinDaoTest {

    private val bitcoin = CoinFactory.create("Bitcoin", "BTC")
    private val ethereum = CoinFactory.create("Ethereum", "ETH")
    private val iot = CoinFactory.create("Iot", "IoT")
    private val crypto = CoinFactory.create("Crypto", "CRO")

    private lateinit var db: CoinDatabase

    @Before
    fun init() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, CoinDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @Test
    fun `GivenCoins WhenInsert InsertIntoDatabase`() = runTest {
        // pre-condition
        assertThat(db.coinDao().count()).isEqualTo(0)

        // execute test
        db.coinDao().insert(listOf(bitcoin, ethereum, iot))

        // verify
        val coins = db.coinDao().selectAll()
        assertThat(coins).containsExactly(bitcoin, ethereum, iot)
        assertThat(db.coinDao().count()).isEqualTo(3)
    }

    @Test
    fun `WhenClear RemoveAllData`() = runTest {
        // init
        db.coinDao().insert(listOf(bitcoin, ethereum, iot))

        // execute test
        db.coinDao().clear()

        // verify
        val coins = db.coinDao().selectAll()
        assertThat(coins).isEmpty()
        assertThat(db.coinDao().count()).isEqualTo(0)
    }

    @Test
    fun `WhenCount ReturnNumberOfSavedRecords`() = runTest {
        // init
        db.coinDao().insert(listOf(bitcoin, ethereum))

        // execute test
        val count = db.coinDao().count()

        // verify
        assertThat(count).isEqualTo(2)
    }

    @Test
    fun `WhenFilterByAsc ReturnListOfFilteredAsc`() = runTest {
        // init
        db.coinDao().insert(listOf(bitcoin, ethereum, iot, crypto))

        // execute test
        val pagingSource = db.coinDao().filterByAsc("o")

        // verify
        val expected = Page(
            data = listOf(bitcoin, crypto, iot),
            prevKey = null,
            nextKey = null,
            itemsBefore = 0,
            itemsAfter = 0
        )
        val actual = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 5,
                placeholdersEnabled = false,
            )
        )
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `WhenFilterByDesc ReturnListOfFilteredDesc`() = runTest {
        // init
        db.coinDao().insert(listOf(bitcoin, ethereum, iot, crypto))

        // execute test
        val pagingSource = db.coinDao().filterByDesc("o")

        // verify
        val expected = Page(
            data = listOf(iot, crypto, bitcoin),
            prevKey = null,
            nextKey = null,
            itemsBefore = 0,
            itemsAfter = 0
        )
        val actual = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 5,
                placeholdersEnabled = false,
            )
        )
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `WhenSelectAllByAsc ReturnAllAsc`() = runTest {
        // init
        db.coinDao().insert(listOf(bitcoin, ethereum, iot, crypto))

        // execute test
        val pagingSource = db.coinDao().selectAllByAsc()

        // verify
        val expected = Page(
            data = listOf(bitcoin, crypto, ethereum, iot),
            prevKey = null,
            nextKey = null,
            itemsBefore = 0,
            itemsAfter = 0
        )
        val actual = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 5,
                placeholdersEnabled = false,
            )
        )
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `WhenSelectAllByDesc ReturnAllDesc`() = runTest {
        // init
        db.coinDao().insert(listOf(bitcoin, ethereum, iot, crypto))

        // execute test
        val pagingSource = db.coinDao().selectAllByDesc()

        // verify
        val expected = Page(
            data = listOf(iot, ethereum, crypto, bitcoin),
            prevKey = null,
            nextKey = null,
            itemsBefore = 0,
            itemsAfter = 0
        )
        val actual = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 5,
                placeholdersEnabled = false,
            )
        )
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `WhenSelectAll ReturnAll`() = runTest {
        // init
        db.coinDao().insert(listOf(bitcoin, ethereum, iot, crypto))

        // execute test
        val actual = db.coinDao().selectAll()

        // verify
        val expected = listOf(iot, ethereum, crypto, bitcoin)
        assertThat(actual).containsExactlyElementsIn(expected)
    }

    // init
    // execute test
    // verify
    @After
    fun tearDown() {
        db.clearAllTables()
        db.close()
    }
}
