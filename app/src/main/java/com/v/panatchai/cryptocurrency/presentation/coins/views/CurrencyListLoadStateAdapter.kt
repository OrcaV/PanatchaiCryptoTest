package com.v.panatchai.cryptocurrency.presentation.coins.views

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

class CurrencyListLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<CurrencyLoadStateViewHolder>() {
    override fun onBindViewHolder(holder: CurrencyLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): CurrencyLoadStateViewHolder {
        return CurrencyLoadStateViewHolder.create(parent, retry)
    }
}
