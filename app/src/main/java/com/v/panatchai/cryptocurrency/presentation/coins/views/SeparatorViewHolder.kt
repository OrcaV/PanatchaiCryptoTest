package com.v.panatchai.cryptocurrency.presentation.coins.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.v.panatchai.cryptocurrency.databinding.ListItemSeparatorBinding

class SeparatorViewHolder(
    binding: ListItemSeparatorBinding
) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun create(parent: ViewGroup) = SeparatorViewHolder(
            ListItemSeparatorBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}
