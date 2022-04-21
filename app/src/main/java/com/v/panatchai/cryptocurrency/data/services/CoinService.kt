package com.v.panatchai.cryptocurrency.data.services

import com.v.panatchai.cryptocurrency.data.models.CoinEntity
import retrofit2.http.GET

interface CoinService {

    @GET("public/v1/coins?skip=0&limit=10")
    suspend fun getCoinList(): List<CoinEntity>
}
