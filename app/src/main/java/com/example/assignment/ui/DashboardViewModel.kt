package com.example.assignment.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.repositories.holdings.network.HoldingResponse
import com.example.repositories.holdings.network.HoldingResponse.Data
import com.example.repositories.holdings.repository.HoldingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(private val holdingsRepository: HoldingsRepository) : ViewModel() {

    private val errorState = MutableStateFlow(false)
    val error: StateFlow<Boolean>
        get() = errorState

    private val userHoldingState = MutableStateFlow(UserHolding(0f, 0f, 0f, 0f))
    val userHolding: StateFlow<UserHolding?>
        get() = userHoldingState

    private val holding = MutableStateFlow<List<Data>>(emptyList())
    val holdingsList: StateFlow<List<Data>>
        get() = holding

    private fun getHoldings(): Flow<List<Data>> {
        return holdingsRepository.latestHolding.stateIn(
            initialValue = emptyList(), scope = viewModelScope, started = SharingStarted.WhileSubscribed(5000)
        ).map { dataList ->
            calculateTotals(dataList)
            holding.value = dataList
            dataList
        }
    }

    fun getHoldingsSize(): Flow<Int> {
        return getHoldings().stateIn(
            initialValue = emptyList(), scope = viewModelScope, started = SharingStarted.WhileSubscribed(5000)
        ).map { dataList ->
            dataList.size
        }
    }

    private fun calculateTotals(dataList: List<Data>) {
        var totalCurrentValue = 0f
        var totalInvestment = 0f
        var todayProfitLoss = 0f
        dataList.forEach {
            totalCurrentValue += (it.quantity * it.ltp).toFloat()
            totalInvestment += (it.avgPrice.toFloat() * it.quantity)
            todayProfitLoss += ((it.close - it.ltp) * it.quantity).toFloat()
        }
        val totalProfitLoss = totalCurrentValue - totalInvestment
        userHoldingState.value = UserHolding(
            totalCurrentValue = totalCurrentValue,
            totalInvestment = totalInvestment,
            todayProfitLoss = todayProfitLoss,
            totalProfitLoss = totalProfitLoss
        )
    }

    data class UserHolding(
        val totalCurrentValue: Float, val totalInvestment: Float, val todayProfitLoss: Float, val totalProfitLoss: Float
    )
}