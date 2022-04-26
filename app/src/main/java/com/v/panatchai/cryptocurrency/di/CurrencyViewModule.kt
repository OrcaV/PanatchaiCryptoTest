package com.v.panatchai.cryptocurrency.di

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.v.panatchai.cryptocurrency.presentation.coins.CurrencyComparator
import com.v.panatchai.cryptocurrency.presentation.coins.views.CurrencyListAdapter
import com.v.panatchai.cryptocurrency.presentation.models.UiModel
import com.v.panatchai.cryptocurrency.presentation.views.SimpleNavigation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.FragmentScoped

@Module
@InstallIn(FragmentComponent::class)
object CurrencyViewModule {

    @FragmentScoped
    @Provides
    fun provideCoinComparator(): DiffUtil.ItemCallback<UiModel> = CurrencyComparator()

    @FragmentScoped
    @Provides
    fun provideGlide(
        @ActivityContext context: Context
    ): GlideRequests = GlideApp.with(context)

    @Suppress("UNCHECKED_CAST")
    @FragmentScoped
    @Provides
    fun provideNavigationComponent(
        fragment: Fragment
    ): SimpleNavigation.NavigationHost<UiModel> {
        return fragment as SimpleNavigation.NavigationHost<UiModel>
    }

    @FragmentScoped
    @Provides
    fun provideCurrencyPagingDataAdapter(
        diffCallback: DiffUtil.ItemCallback<UiModel>,
        glide: GlideRequests,
        navHost: SimpleNavigation.NavigationHost<UiModel>
    ): PagingDataAdapter<UiModel, RecyclerView.ViewHolder> =
        CurrencyListAdapter(diffCallback, glide) { model ->
            navHost.navigateBy(model)
        }
}
