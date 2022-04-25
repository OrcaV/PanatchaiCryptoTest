package com.v.panatchai.cryptocurrency.di

import androidx.paging.PagingData
import com.v.panatchai.cryptocurrency.data.repositories.coins.ICoinRepository
import com.v.panatchai.cryptocurrency.domain.UseCase
import com.v.panatchai.cryptocurrency.domain.coins.CurrencyArgument
import com.v.panatchai.cryptocurrency.domain.coins.CurrencyListUseCase
import com.v.panatchai.cryptocurrency.domain.models.coins.Coin
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @ViewModelScoped
    @Provides
    fun provideCurrencyListUseCase(
        coinRepo: ICoinRepository
    ): UseCase<CurrencyArgument, Flow<PagingData<Coin>>> = CurrencyListUseCase(coinRepo)
}
