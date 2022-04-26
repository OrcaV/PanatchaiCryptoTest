package com.v.panatchai.cryptocurrency.data.mediators

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.Pager
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.v.panatchai.cryptocurrency.constants.CoinServiceConfig
import com.v.panatchai.cryptocurrency.data.database.CoinDatabase
import com.v.panatchai.cryptocurrency.data.models.coins.CoinEntity
import com.v.panatchai.cryptocurrency.data.services.CoinService

/**
 * A part of Paging3 library and architecture, which first query data from the local database and,
 * if not presented, the [RemoteMediator] will fetch more data from the network.
 */
@OptIn(ExperimentalPagingApi::class)
class CoinServiceMediator(
    private val database: CoinDatabase,
    private val coinService: CoinService,
    private val pageSize: Int
) : RemoteMediator<Int, CoinEntity>() {

    /**
     * Provide a simple paging mechanism, which points to the last item fetched from the network.
     * [startIndex] increments by [pageSize].
     */
    private var startIndex = 0

    /**
     * Access to the Coin table.
     */
    private val coinDao = database.coinDao()

    override suspend fun initialize(): InitializeAction {
        // If there are some data stored locally, do not fetch any new data from the network.
        startIndex = coinDao.count()
        return if (startIndex > 0) {
            // There are some data stored locally, do not fetch new data from the network.
            // In this simple example, I imply that cache never expire.
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            // No data stored locally, fetch from the network.
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    @Suppress("OVERLOADS_WITHOUT_DEFAULT_ARGUMENTS")
    @JvmOverloads
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CoinEntity>
    ): MediatorResult {
        return try {
            val newIndex = when (loadType) {
                LoadType.REFRESH -> 0 // start reloading new set of data
                LoadType.APPEND -> startIndex + pageSize // start where it is left off
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true) // not supported
            }

            /**
             * In case of REFRESH and APPEND, fetch the new data from the network accordingly, where
             * 1st parameter [startIndex] represents the offset, and
             * 2nd parameter [pageSize] presents the number of items expected.
             */
            val coinResponse = coinService.getCoinList(startIndex, pageSize)
            val coins = coinResponse.coins

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    // Remove all local data in case of REFRESH
                    coinDao.clear()
                }

                // Save the new data locally
                coinDao.insert(coins)
            }

            // when success, move the index to the next position
            startIndex = newIndex
            MediatorResult.Success(coins.size < pageSize || startIndex >= HARDCODE_LIMIT)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    companion object {
        /**
         * If you are wondering why setting the limit, the answer is that there is no need for this.
         * The Paging3 will keep loading the data from the api indefinitely in the background, however,
         * since it is not my owm api, I set the limit so to not to use up too much data and to overcome
         * its limitation.
         *
         * In case when you search for, for example, 'coin' and the results are less than
         * 2 x [CoinServiceConfig.PAGE_SIZE], the Paging3 will keep fetching data from the api
         * indefinitely if there is no limit or until it fulfils at least
         * 2 x [CoinServiceConfig.PAGE_SIZE] or whatever is configured inside the [Pager].
         */
        private const val HARDCODE_LIMIT = 100
    }
}
