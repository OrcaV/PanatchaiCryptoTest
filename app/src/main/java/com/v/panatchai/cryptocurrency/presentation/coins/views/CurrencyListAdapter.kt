package com.v.panatchai.cryptocurrency.presentation.coins.views

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.v.panatchai.cryptocurrency.di.GlideRequests
import com.v.panatchai.cryptocurrency.presentation.models.CurrencyModel
import com.v.panatchai.cryptocurrency.presentation.models.UiModel

class CurrencyListAdapter constructor(
    diffCallback: DiffUtil.ItemCallback<UiModel>,
    private val glide: GlideRequests,
    private val onItemClicked: (UiModel) -> Unit
) : PagingDataAdapter<UiModel, RecyclerView.ViewHolder>(diffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_CURRENCY -> CurrencyViewHolder.create(parent, glide, onItemClicked)
            else -> SeparatorViewHolder.create(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // Note that item may be null. ViewHolder must support binding a
        // null item as a placeholder.
        val item = getItem(position)
        if (item is CurrencyModel) {
            (holder as CurrencyViewHolder).bind(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is CurrencyModel -> TYPE_CURRENCY
            else -> TYPE_SEPARATOR
        }
    }

    companion object {
        private const val TYPE_CURRENCY = 0
        private const val TYPE_SEPARATOR = 1
    }
}
