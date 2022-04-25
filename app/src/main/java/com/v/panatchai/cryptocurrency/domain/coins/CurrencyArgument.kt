package com.v.panatchai.cryptocurrency.domain.coins

import com.v.panatchai.cryptocurrency.domain.models.UseCaseArgument
import com.v.panatchai.cryptocurrency.enums.OrderBy

/**
 * Optional
 * Represent the query for list of currencies.
 *
 * @param orderBy Arrange the results by either [OrderBy.ASC] or [OrderBy.DESC].
 * @param filter Any text to filter the results.
 */
data class CurrencyArgument(
    val orderBy: OrderBy,
    val filter: String
) : UseCaseArgument()
