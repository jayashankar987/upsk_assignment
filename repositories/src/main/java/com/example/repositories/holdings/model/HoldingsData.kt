package com.example.repositories.holdings.model

data class HoldingsData(
    private val companyName: String,
    private val quantity: Long,
    private val ltp: Float,
    private val profitAndLoss: Float
)