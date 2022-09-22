package com.example.repositories.holdings.repository

import com.example.repositories.BuildConfig
import org.junit.Test

class MockRetrofitClientTest {

    @Test
    fun testRetrofitInstance() {
        val retrofitInstance = MockRetrofitClient().retrofit
        assert(retrofitInstance.baseUrl().toString() == BuildConfig.BASE_URL)
    }

    @Test
    fun testResponseFromApi() {

    }
}