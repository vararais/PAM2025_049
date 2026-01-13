package com.example.savitapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.savitapp.model.History
import com.example.savitapp.model.Stuff
import com.example.savitapp.repository.StuffRepository
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface DetailUiState {
    object Loading : DetailUiState
    data class Success(val stuff: Stuff, val historyList: List<History>) : DetailUiState
    data class Error(val message: String) : DetailUiState
}

class DetailViewModel(private val repository: StuffRepository) : ViewModel() {
    var detailUiState: DetailUiState by mutableStateOf(DetailUiState.Loading)
        private set

    fun getStuffDetail(userId: Int, stuffId: Int) {
        viewModelScope.launch {
            detailUiState = DetailUiState.Loading
            try {
                // 1. Ambil semua barang user, lalu cari yang ID-nya cocok
                val responseStuff = repository.getAllStuff(userId)
                val stuff = responseStuff.body()?.find { it.stuffId == stuffId }

                // 2. Ambil History transaksi barang tersebut
                val responseHistory = repository.getHistory(stuffId)
                val historyList = responseHistory.body() ?: emptyList()

                if (stuff != null) {
                    detailUiState = DetailUiState.Success(stuff, historyList)
                } else {
                    detailUiState = DetailUiState.Error("Barang tidak ditemukan")
                }
            } catch (e: IOException) {
                detailUiState = DetailUiState.Error("Koneksi Error: ${e.message}")
            }
        }
    }

    fun deleteStuff(stuffId: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val response = repository.deleteStuff(stuffId)
                if (response.isSuccessful) {
                    onSuccess()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}