package com.v.panatchai.cryptocurrency.mappers

import com.google.common.truth.Truth.assertThat
import com.v.panatchai.cryptocurrency.data.models.coins.CoinEntity
import com.v.panatchai.cryptocurrency.domain.models.coins.Coin
import com.v.panatchai.cryptocurrency.presentation.models.CurrencyModel
import org.junit.Test

class CoinEntityMapperKtTest {

    @Test
    fun `GivenCoinEntity WhenMap ReturnCoin`() {
        // init mock
        val entity = CoinEntity("id", "name", "symbol", "icon")

        // execute test
        val actual = entity.map()

        // verify
        val expected = Coin("id", "name", "symbol", "icon")
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `GivenCoin WhenMap ReturnCurrencyModel`() {
        // init mock
        val entity = Coin("id", "name", "symbol", "icon")

        // execute test
        val actual = entity.map()

        // verify
        val expected = CurrencyModel("id", "name", "symbol", "icon")
        assertThat(actual).isEqualTo(expected)
    }
}
