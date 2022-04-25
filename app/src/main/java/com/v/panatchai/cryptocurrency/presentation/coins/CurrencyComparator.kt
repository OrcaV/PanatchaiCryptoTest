package com.v.panatchai.cryptocurrency.presentation.coins

import androidx.recyclerview.widget.DiffUtil
import com.v.panatchai.cryptocurrency.presentation.models.CurrencyModel
import com.v.panatchai.cryptocurrency.presentation.models.UiModel

class CurrencyComparator : DiffUtil.ItemCallback<UiModel>() {
    override fun areItemsTheSame(oldItem: UiModel, newItem: UiModel): Boolean {
        val isSameType = oldItem::class == newItem::class
        val isSameItem = if (oldItem is CurrencyModel && newItem is CurrencyModel) {
            oldItem.id == newItem.id
        } else isSameType && oldItem !is CurrencyModel
        return isSameType && isSameItem
    }

    override fun areContentsTheSame(oldItem: UiModel, newItem: UiModel): Boolean {
        return oldItem == newItem
    }
}
