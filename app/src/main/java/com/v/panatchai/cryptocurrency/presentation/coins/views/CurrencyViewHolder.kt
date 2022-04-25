package com.v.panatchai.cryptocurrency.presentation.coins.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.v.panatchai.cryptocurrency.databinding.ListItemCurrencyBinding
import com.v.panatchai.cryptocurrency.di.GlideRequests
import com.v.panatchai.cryptocurrency.presentation.models.CurrencyModel
import com.v.panatchai.cryptocurrency.presentation.models.UiModel

class CurrencyViewHolder(
    private val binding: ListItemCurrencyBinding,
    private val glide: GlideRequests
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: UiModel?) {
        /**
         * Want to handle the PlaceHolder when the [UiModel] is null, however,
         * Not enough time.
         */
        (model as? CurrencyModel)?.let { currencyModel ->
            binding.model = currencyModel
            glide.load(currencyModel.icon)
                .centerCrop()
                .placeholder(android.R.drawable.ic_dialog_info)
                .into(binding.listItemCurrencyImage)
            binding.executePendingBindings()
        } ?: run {
            glide.clear(binding.listItemCurrencyImage)
        }
    }

    companion object {
        fun create(parent: ViewGroup, glide: GlideRequests) = CurrencyViewHolder(
            ListItemCurrencyBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            glide
        )
    }
}
