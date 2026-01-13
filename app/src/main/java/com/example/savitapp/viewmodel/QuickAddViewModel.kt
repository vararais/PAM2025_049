package com.example.savitapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.savitapp.repository.StuffRepository
import kotlinx.coroutines.launch

class QuickAddViewModel(private val repository: StuffRepository) : ViewModel() {

    var nominalInput by mutableStateOf("")
        private set

    fun updateNominal(value: String) {
        nominalInput = value
    }

    fun saveTransaction(stuffId: Int, onSuccess: () -> Unit) {
        val nominal = nominalInput.toLongOrNull() ?: 0L
        if (nominal > 0) {
            viewModelScope.launch {
                try {
                    val response = repository.addTransaction(stuffId, nominal)
                    if (response.isSuccessful) {
                        nominalInput = "" // Reset input
                        onSuccess() // Callback agar Dashboard refresh data
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}