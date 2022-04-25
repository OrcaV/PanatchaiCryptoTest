package com.v.panatchai.cryptocurrency.presentation.coins.views

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.v.panatchai.cryptocurrency.domain.UseCase
import com.v.panatchai.cryptocurrency.domain.coins.CurrencyArgument
import com.v.panatchai.cryptocurrency.domain.models.coins.Coin
import com.v.panatchai.cryptocurrency.enums.OrderBy
import com.v.panatchai.cryptocurrency.mappers.map
import com.v.panatchai.cryptocurrency.presentation.models.ListSeparator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

/**
 * Orchestrate the Currency UI.
 */
@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val currencyList: UseCase<CurrencyArgument, Flow<PagingData<Coin>>>, // <-- depends on abstraction
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    init {
        if (!savedStateHandle.contains(KEY_COMBINE_STATE)) {
            savedStateHandle[KEY_FILTER] = DEFAULT_FILTER
            savedStateHandle[KEY_ORDER_BY] = DEFAULT_ORDER_BY
            savedStateHandle[KEY_COMBINE_STATE] = DEFAULT_STATE
        }
    }

    /**
     * Represent the current user's selection
     */
    val orderBy = savedStateHandle.getStateFlow(KEY_ORDER_BY, DEFAULT_ORDER_BY)

    /**
     * Represent the current Currency list and response to changes in both
     * the filter and order.
     */
    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val currencies = savedStateHandle.getStateFlow(KEY_COMBINE_STATE, DEFAULT_STATE)
        .debounce(350) // prevent entering new filters too quickly
        .distinctUntilChanged { old, new -> old.equals(new, true) } // prevent the same filter
        .flatMapLatest {
            val filter = savedStateHandle.get<String>(KEY_FILTER)!!
            val orderBy = savedStateHandle.get<OrderBy>(KEY_ORDER_BY)!!
            currencyList(CurrencyArgument(orderBy, filter))
        }
        // Only to add a list item separator.
        .map { pagingData ->
            pagingData.map(Coin::map)
                .insertSeparators { before, after ->
                    when {
                        before == null -> null
                        after == null -> null
                        else -> ListSeparator("${before.name}-${after.name}")
                    }
                }
        }
        .flowOn(Dispatchers.IO) // The ⬆️ stream will be running on IO
        .cachedIn(viewModelScope) // cache in ViewModel

    /**
     * Set the new filter.
     *
     * @param filter any new filters, null or empty -> no filter
     */
    fun filter(filter: String?) {
        savedStateHandle[KEY_FILTER] = filter ?: DEFAULT_FILTER
        combineState(
            savedStateHandle.get<String>(KEY_FILTER)!!,
            savedStateHandle.get<OrderBy>(KEY_ORDER_BY)!!
        )
    }

    /**
     * Toggle the sorting order between A-Z and Z-A.
     */
    fun toggleOrderBy() {
        when (savedStateHandle.get<OrderBy>(KEY_ORDER_BY)) {
            OrderBy.ASC -> savedStateHandle[KEY_ORDER_BY] = OrderBy.DESC
            else -> savedStateHandle[KEY_ORDER_BY] = OrderBy.ASC
        }
        combineState(
            savedStateHandle.get<String>(KEY_FILTER)!!,
            savedStateHandle.get<OrderBy>(KEY_ORDER_BY)!!
        )
    }

    private fun combineState(filter: String, orderBy: OrderBy) {
        savedStateHandle[KEY_COMBINE_STATE] = "$filter ${orderBy.name}"
    }

    companion object {
        const val KEY_FILTER = "filterBy"
        const val KEY_ORDER_BY = "orderBy"
        const val KEY_COMBINE_STATE = "combineState"
        const val DEFAULT_FILTER = ""
        val DEFAULT_ORDER_BY = OrderBy.ASC
        val DEFAULT_STATE = "$DEFAULT_FILTER $DEFAULT_ORDER_BY"
    }
}
