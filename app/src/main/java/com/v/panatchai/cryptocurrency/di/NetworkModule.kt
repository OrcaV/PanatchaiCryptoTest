package com.v.panatchai.cryptocurrency.di

import com.v.panatchai.cryptocurrency.BuildConfig
import com.v.panatchai.cryptocurrency.data.services.CoinService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.COIN_API)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    @Singleton
    @Provides
    fun provideCoinService(
        retrofit: Retrofit
    ): CoinService = retrofit.create(CoinService::class.java)
}
