package com.v.panatchai.cryptocurrency.data.repositories.coins

import androidx.paging.PagingData
import com.v.panatchai.cryptocurrency.data.models.coins.CoinEntity
import com.v.panatchai.cryptocurrency.enums.OrderBy
import kotlinx.coroutines.flow.Flow

/**
 * Represent the Coin Repository which manages the data retrievals and local storage.
 */
interface ICoinRepository {

    /**
     * Returns [CoinEntity] upon request.
     *
     * @param orderBy Arrange the results by either [OrderBy.ASC] or [OrderBy.DESC].
     * @param filter Any text to filter the results.
     *
     * @return [Flow] of [PagingData] of [CoinEntity]
     */
    fun fetchCoins(orderBy: OrderBy, filter: String): Flow<PagingData<CoinEntity>>
}
