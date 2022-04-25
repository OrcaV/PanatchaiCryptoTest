package com.v.panatchai.cryptocurrency.data.models.coins

import com.squareup.moshi.Json

/**
 * Represent the API response.
 */
data class CoinResponse(
    @field:Json(name = "coins")
    val coins: List<CoinEntity>
)
