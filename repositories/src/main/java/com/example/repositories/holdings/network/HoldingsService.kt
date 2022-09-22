package com.example.repositories.holdings.network

import com.example.repositories.Constants
import retrofit2.Response
import retrofit2.http.GET

interface HoldingsService {

    @GET(Constants.API_HOLDING)
    suspend fun getHoldings(): Response<HoldingResponse>

}