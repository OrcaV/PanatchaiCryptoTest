package com.v.panatchai.cryptocurrency.presentation.bindings

import android.content.Context
import android.widget.ImageButton
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.v.panatchai.cryptocurrency.R
import com.v.panatchai.cryptocurrency.enums.OrderBy
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(maxSdk = 30)
class CurrencyBindingAdapterKtTest {

    private lateinit var context: Context

    @Before
    fun init() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun `GivenAsc WhenOrderBy Set ic_a2z`() {
        // init mocks
        val imageButton = mock<ImageButton>()
        whenever(imageButton.context).thenReturn(context)

        // execute test
        imageButton.orderBy(OrderBy.ASC)

        // verify
        verify(imageButton).setImageResource(R.drawable.ic_a2z)
        verify(imageButton).contentDescription = context.getString(
            R.string.currency_sort_description,
            OrderBy.ASC.name
        )
    }

    @Test
    fun `GivenDesc WhenOrderBy Set ic_z2a`() {
        // init mocks
        val imageButton = mock<ImageButton>()
        whenever(imageButton.context).thenReturn(context)

        // execute test
        imageButton.orderBy(OrderBy.DESC)

        // verify
        verify(imageButton).setImageResource(R.drawable.ic_z2a)
        verify(imageButton).contentDescription = context.getString(
            R.string.currency_sort_description,
            OrderBy.DESC.name
        )
    }
}
