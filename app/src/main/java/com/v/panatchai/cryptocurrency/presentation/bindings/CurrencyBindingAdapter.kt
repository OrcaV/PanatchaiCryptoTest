package com.v.panatchai.cryptocurrency.presentation.bindings

import android.widget.ImageButton
import androidx.databinding.BindingAdapter
import com.v.panatchai.cryptocurrency.R
import com.v.panatchai.cryptocurrency.enums.OrderBy

@BindingAdapter("orderBy")
fun ImageButton.orderBy(orderBy: OrderBy) {
    when (orderBy) {
        OrderBy.ASC -> setImageResource(R.drawable.ic_a2z)
        else -> setImageResource(R.drawable.ic_z2a)
    }
    contentDescription = context.getString(
        R.string.currency_sort_description,
        orderBy.name
    )
}
