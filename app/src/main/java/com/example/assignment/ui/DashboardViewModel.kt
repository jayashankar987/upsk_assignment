package com.example.assignment.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.repositories.holdings.network.HoldingResponse.Data
import com.example.repositories.holdings.repository.HoldingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(private val holdingsRepository: HoldingsRepository) : ViewModel() {

    private val _state = MutableStateFlow(emptyList<Data>())
    val state: StateFlow<List<Data>>
        get() = _state

    private val errorState = MutableStateFlow(false)
    val error: MutableStateFlow<Boolean>
        get() = errorState

    init {
        getHoldings()
    }

    fun getHoldings() {
        viewModelScope.launch {
            var holdings: List<Data>
            try {
                holdings = holdingsRepository.getUserHoldings()
                errorState.value = false
            } catch (e: Exception) {
                holdings = holdingsRepository.getOfflineDataIfAvailable()
                errorState.value = true
            }
            _state.value = holdings
        }
    }

}