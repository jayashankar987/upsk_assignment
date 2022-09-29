package com.example.repositories.di

import com.example.repositories.BuildConfig
import com.example.repositories.holdings.network.HoldingsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.internal.managers.ApplicationComponentManager
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HoldingsModule {

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit.Builder =
        Retrofit.Builder().baseUrl(BuildConfig.BASE_URL).addConverterFactory(MoshiConverterFactory.create())

    @Singleton
    @Provides
    fun providesHoldingsApi(builder: Retrofit.Builder): HoldingsService = builder.build().create(HoldingsService::class.java)
}