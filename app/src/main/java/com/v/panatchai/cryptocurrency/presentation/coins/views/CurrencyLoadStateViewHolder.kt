package com.v.panatchai.cryptocurrency.presentation.coins.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.v.panatchai.cryptocurrency.R
import com.v.panatchai.cryptocurrency.databinding.ListItemCurrencyLoadStateFooterBinding

class CurrencyLoadStateViewHolder(
    private val binding: ListItemCurrencyLoadStateFooterBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.retryButton.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            binding.errorText.text = loadState.error.localizedMessage
        }
        binding.progressBar.isVisible = loadState is LoadState.Loading
        binding.retryButton.isVisible = loadState is LoadState.Error
        binding.errorText.isVisible = loadState is LoadState.Error
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): CurrencyLoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_currency_load_state_footer, parent, false)
            val binding = ListItemCurrencyLoadStateFooterBinding.bind(view)
            return CurrencyLoadStateViewHolder(binding, retry)
        }
    }
}
