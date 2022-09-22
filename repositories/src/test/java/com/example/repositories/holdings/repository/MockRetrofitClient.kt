package com.example.repositories.holdings.repository

import com.example.repositories.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MockRetrofitClient {
    val retrofit: Retrofit by lazy {
        Retrofit.Builder().baseUrl(BuildConfig.BASE_URL).addConverterFactory(MoshiConverterFactory.create()).build()
    }
}