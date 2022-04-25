package com.v.panatchai.cryptocurrency.data.repositories.coins

import com.v.panatchai.cryptocurrency.data.models.coins.CoinEntity

object CoinFactory {
    fun create(
        name: String,
        symbol: String,
        id: String = name,
        icon: String = name
    ) = CoinEntity(
        id = id,
        name = name,
        symbol = symbol,
        icon = icon
    )
}
