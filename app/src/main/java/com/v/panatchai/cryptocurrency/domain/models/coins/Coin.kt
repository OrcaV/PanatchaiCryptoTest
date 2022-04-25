package com.v.panatchai.cryptocurrency.domain.models.coins

import com.v.panatchai.cryptocurrency.domain.models.BaseModel

/**
 * Optional
 * Represent the domain Currency.
 *
 * @param id Currency Id.
 * @param name Currency Name e.g. Bitcoin.
 * @param symbol Currency Symbol e.g. BTC.
 * @param icon Image Url of the Currency.
 */
data class Coin(
    val id: String,
    val name: String,
    val symbol: String,
    val icon: String
) : BaseModel()
