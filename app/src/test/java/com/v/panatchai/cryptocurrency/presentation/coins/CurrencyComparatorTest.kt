package com.v.panatchai.cryptocurrency.presentation.coins

import androidx.recyclerview.widget.DiffUtil
import com.google.common.truth.Truth.assertThat
import com.v.panatchai.cryptocurrency.presentation.models.CurrencyModel
import com.v.panatchai.cryptocurrency.presentation.models.ListSeparator
import com.v.panatchai.cryptocurrency.presentation.models.UiModel
import org.junit.Before
import org.junit.Test

class CurrencyComparatorTest {

    private lateinit var diff: DiffUtil.ItemCallback<UiModel>

    @Before
    fun init() {
        diff = CurrencyComparator()
    }

    @Test
    fun `GivenDifferentType areItemsTheSame ReturnFalse`() {
        val model1 = CurrencyModel(
            "id", "name", "symbol", "icon"
        )
        val model2 = ListSeparator("")

        assertThat(diff.areItemsTheSame(model1, model2)).isFalse()
    }

    @Test
    fun `GivenSameListSeparator areItemsTheSame ReturnTrue`() {
        val model1 = ListSeparator("1")
        val model2 = model1.copy()

        assertThat(diff.areItemsTheSame(model1, model2)).isTrue()
    }

    @Test
    fun `GivenDifferentListSeparator areItemsTheSame ReturnTrue`() {
        val model1 = ListSeparator("1")
        val model2 = ListSeparator("2")

        assertThat(diff.areItemsTheSame(model1, model2)).isTrue()
    }

    @Test
    fun `GivenSameCurrencyModel areItemsTheSame ReturnTrue`() {
        val model1 = CurrencyModel(
            "id", "name", "symbol", "icon"
        )
        val model2 = model1.copy()

        assertThat(diff.areItemsTheSame(model1, model2)).isTrue()
    }

    @Test
    fun `GivenDifferentCurrencyModel areItemsTheSame ReturnFalse`() {
        val model1 = CurrencyModel(
            "id1", "name", "symbol", "icon"
        )
        val model2 = model1.copy(id = "id2")

        assertThat(diff.areItemsTheSame(model1, model2)).isFalse()
    }

    @Test
    fun `GivenSameCurrencyModel areContentsTheSame ReturnTrue`() {
        val model1 = CurrencyModel(
            "id", "name", "symbol", "icon"
        )
        val model2 = model1.copy()

        assertThat(diff.areContentsTheSame(model1, model2)).isTrue()
    }

    @Test
    fun `GivenDifferentCurrencyModel areContentsTheSame ReturnFalse`() {
        val model1 = CurrencyModel(
            "id1", "name", "symbol", "icon"
        )
        val model2 = model1.copy(id = "id2")

        assertThat(diff.areContentsTheSame(model1, model2)).isFalse()
    }

    @Test
    fun `GivenSameListSeparator areContentsTheSame ReturnTrue`() {
        val model1 = ListSeparator("1")
        val model2 = model1.copy()

        assertThat(diff.areContentsTheSame(model1, model2)).isTrue()
    }

    @Test
    fun `GivenDifferentListSeparator areContentsTheSame ReturnFalse`() {
        val model1 = ListSeparator("1")
        val model2 = ListSeparator("2")

        assertThat(diff.areContentsTheSame(model1, model2)).isFalse()
    }
}
