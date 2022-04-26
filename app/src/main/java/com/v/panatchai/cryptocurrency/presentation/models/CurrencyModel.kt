package com.v.panatchai.cryptocurrency.presentation.models

import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

/**
 * Represent the UI Currency model.
 */
@Parcelize
@Keep
data class CurrencyModel(
    val id: String,
    val name: String,
    val symbol: String,
    val icon: String
) : UiModel()

/**
 * Represent the UI list separator.
 */
@Parcelize
@Keep
data class ListSeparator(val tag: String) : UiModel()
