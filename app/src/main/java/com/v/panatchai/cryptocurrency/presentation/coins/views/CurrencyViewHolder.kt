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
    private val glide: GlideRequests,
    onItemClicked: (UiModel) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        // Setting onClick when create the ViewHolder is the most efficient way, in contradiction to,
        // what you may have seen where people set the onClick inside the `bind()` method, which gets
        // called the time.
        binding.root.setOnClickListener {
            onItemClicked(binding.model as UiModel)
        }
    }

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
        fun create(
            parent: ViewGroup,
            glide: GlideRequests,
            onItemClicked: (UiModel) -> Unit
        ) = CurrencyViewHolder(
            ListItemCurrencyBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            glide,
            onItemClicked
        )
    }
}
