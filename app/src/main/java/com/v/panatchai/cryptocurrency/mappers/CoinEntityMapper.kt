package com.v.panatchai.cryptocurrency.mappers

import com.v.panatchai.cryptocurrency.data.models.coins.CoinEntity
import com.v.panatchai.cryptocurrency.domain.models.coins.Coin
import com.v.panatchai.cryptocurrency.presentation.models.CurrencyModel

fun CoinEntity.map() = Coin(
    id = id,
    name = name,
    symbol = symbol,
    icon = icon
)

fun Coin.map() = CurrencyModel(
    id = id,
    name = name,
    symbol = symbol,
    icon = icon
)
