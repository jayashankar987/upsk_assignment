package com.example.repositories.holdings.repository

import com.example.repositories.holdings.network.HoldingsService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.junit.Test

internal class HoldingsRepositoryTest {

    private val retrofitClient = MockRetrofitClient().retrofit
    private val holdingsService = retrofitClient.create(HoldingsService::class.java)

    @Test
    fun getUserHoldingsTest() {

        CoroutineScope(Dispatchers.IO).launch {
            val holdingsResponse = holdingsService.getHoldings()
            val errorBody = holdingsResponse.errorBody()
            assert(errorBody == null)

            val responseWrapper = holdingsResponse.body()
            assert(responseWrapper != null)
            assert(holdingsResponse.code() == 200)
        }
    }
}