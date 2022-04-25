package com.v.panatchai.cryptocurrency.mappers

import com.v.panatchai.cryptocurrency.data.models.coins.CoinEntity
import com.v.panatchai.cryptocurrency.domain.models.coins.Coin

fun CoinEntity.map() = Coin(
    id = id,
    name = name,
    symbol = symbol,
    icon = icon
)
