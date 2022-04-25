package com.v.panatchai.cryptocurrency.di

import android.content.Context
import androidx.room.Room
import com.v.panatchai.cryptocurrency.BuildConfig
import com.v.panatchai.cryptocurrency.data.database.CoinDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideCoinDatabase(
        @ApplicationContext context: Context
    ): CoinDatabase = Room.databaseBuilder(
        context,
        CoinDatabase::class.java,
        BuildConfig.COIN_DATABASE
    )
        .fallbackToDestructiveMigration()
        .build()
}
