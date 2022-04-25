package com.v.panatchai.cryptocurrency.data.repositories.coins

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.RemoteMediator
import com.v.panatchai.cryptocurrency.data.database.CoinDatabase
import com.v.panatchai.cryptocurrency.data.models.coins.CoinEntity
import com.v.panatchai.cryptocurrency.data.repositories.BaseRepository
import com.v.panatchai.cryptocurrency.enums.OrderBy

/**
 * A concrete implementation of Coin Repository which manages the data retrievals and local storage.
 * This only fetches new data from the network if they are not present locally with the help of
 * [Pager].
 */
@OptIn(ExperimentalPagingApi::class)
class CoinRepository(
    private val database: CoinDatabase, // <-- depends on abstract
    private val remoteMediator: RemoteMediator<Int, CoinEntity>, // <-- depends on abstract
    private val pageSize: Int,
    private val prefetchDistance: Int
) : BaseRepository(), ICoinRepository {

    override fun fetchCoins(orderBy: OrderBy, filter: String) = Pager(
        config = PagingConfig(
            pageSize = pageSize,
            prefetchDistance = prefetchDistance,
            enablePlaceholders = true
        ),
        remoteMediator = remoteMediator,
    ) {
        when (orderBy) {
            OrderBy.ASC -> {
                if (filter.isEmpty()) {
                    database.coinDao().selectAllByAsc()
                } else {
                    database.coinDao().filterByAsc(filter)
                }
            }
            OrderBy.DESC -> {
                if (filter.isEmpty()) {
                    database.coinDao().selectAllByDesc()
                } else {
                    database.coinDao().filterByDesc(filter)
                }
            }
        }
    }.flow // convert to Flow
}
