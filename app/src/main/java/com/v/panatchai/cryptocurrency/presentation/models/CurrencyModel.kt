package com.v.panatchai.cryptocurrency.presentation.models

/**
 * Represent the UI Currency model.
 */
data class CurrencyModel(
    val id: String,
    val name: String,
    val symbol: String,
    val icon: String
) : UiModel()

/**
 * Represent the UI list separator.
 */
data class ListSeparator(val tag: String) : UiModel()
