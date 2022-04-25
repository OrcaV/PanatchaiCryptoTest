package com.v.panatchai.cryptocurrency.data.services

import com.v.panatchai.cryptocurrency.data.models.coins.CoinResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Represent the network/api service.
 */
interface CoinService {

    /**
     * Fetch list of coins.
     *
     * @param startId numbers of items to be omitted from the query.
     * @param pageSize numbers of items expected to be returned.
     *
     * @return [CoinResponse]
     */
    @GET("public/v1/coins")
    suspend fun getCoinList(
        @Query("skip") startId: Int,
        @Query("limit") pageSize: Int
    ): CoinResponse
}
