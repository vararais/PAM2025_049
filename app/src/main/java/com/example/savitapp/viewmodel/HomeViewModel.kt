package com.example.savitapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.savitapp.model.Stuff
import com.example.savitapp.repository.StuffRepository
import kotlinx.coroutines.launch
import java.io.IOException

// State untuk UI Home
sealed interface HomeUiState {
    object Loading : HomeUiState
    data class Success(val stuffList: List<Stuff>) : HomeUiState
    data class Error(val message: String) : HomeUiState
}

class HomeViewModel(private val repository: StuffRepository) : ViewModel() {
    var homeUiState: HomeUiState by mutableStateOf(HomeUiState.Loading)
        private set

    // Fungsi untuk mengambil data barang berdasarkan User ID
    fun getStuffList(userId: Int) {
        viewModelScope.launch {
            homeUiState = HomeUiState.Loading
            try {
                val response = repository.getAllStuff(userId)
                if (response.isSuccessful) {
                    homeUiState = HomeUiState.Success(response.body() ?: emptyList())
                } else {
                    homeUiState = HomeUiState.Error("Gagal memuat data")
                }
            } catch (e: IOException) {
                homeUiState = HomeUiState.Error("Koneksi Error: ${e.message}")
            }
        }
    }
}