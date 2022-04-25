package com.v.panatchai.cryptocurrency.domain.coins

import androidx.paging.PagingData
import androidx.paging.map
import com.v.panatchai.cryptocurrency.data.models.coins.CoinEntity
import com.v.panatchai.cryptocurrency.data.repositories.coins.ICoinRepository
import com.v.panatchai.cryptocurrency.domain.UseCase
import com.v.panatchai.cryptocurrency.domain.models.coins.Coin
import com.v.panatchai.cryptocurrency.mappers.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Optional
 * Represent a domain UseCase for fetching currencies.
 *
 * @param coinRepo Coin Repository.
 */
class CurrencyListUseCase(
    private val coinRepo: ICoinRepository // <-- depends on abstraction
) : UseCase<CurrencyArgument, Flow<PagingData<Coin>>>() {

    override suspend fun invoke(vararg args: CurrencyArgument): Flow<PagingData<Coin>> {
        if (args.isEmpty()) {
            throw IllegalArgumentException("Invalid Currency Argument")
        }
        return coinRepo.fetchCoins(args[0].orderBy, args[0].filter)
            .map { it.map(CoinEntity::map) }
    }
}
