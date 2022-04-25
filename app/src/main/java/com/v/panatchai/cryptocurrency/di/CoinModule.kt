package com.v.panatchai.cryptocurrency.di

import androidx.paging.ExperimentalPagingApi
import androidx.paging.RemoteMediator
import com.v.panatchai.cryptocurrency.constants.CoinServiceConfig
import com.v.panatchai.cryptocurrency.data.database.CoinDatabase
import com.v.panatchai.cryptocurrency.data.mediators.CoinServiceMediator
import com.v.panatchai.cryptocurrency.data.models.coins.CoinEntity
import com.v.panatchai.cryptocurrency.data.repositories.coins.CoinRepository
import com.v.panatchai.cryptocurrency.data.repositories.coins.ICoinRepository
import com.v.panatchai.cryptocurrency.data.services.CoinService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
@OptIn(ExperimentalPagingApi::class)
object CoinModule {

    @ViewModelScoped
    @Provides
    fun provideCoinRemoteMediator(
        database: CoinDatabase,
        coinService: CoinService
    ): RemoteMediator<Int, CoinEntity> =
        CoinServiceMediator(database, coinService, CoinServiceConfig.PAGE_SIZE)

    @ViewModelScoped
    @Provides
    fun provideICoinRepository(
        database: CoinDatabase,
        remoteMediator: RemoteMediator<Int, CoinEntity>,
    ): ICoinRepository = CoinRepository(
        database,
        remoteMediator,
        CoinServiceConfig.PAGE_SIZE,
        CoinServiceConfig.PAGE_SIZE
    )
}
