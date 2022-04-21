package com.v.panatchai.cryptocurrency.data.models

import com.squareup.moshi.Json

data class CoinEntity(
    @field:Json(name = "id") val id: String,
    @field:Json(name = "name") val name: String,
    @field:Json(name = "symbol") val symbol: String,
    @field:Json(name = "icon") val icon: String = ""
)
