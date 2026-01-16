package com.example.savitapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.savitapp.model.Stuff
import com.example.savitapp.repository.StuffRepository // <-- PENTING: IMPORT INI
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface HomeUiState {
    data class Success(val stuffList: List<Stuff>) : HomeUiState
    data class Error(val message: String) : HomeUiState
    object Loading : HomeUiState
}

class HomeViewModel(private val repository: StuffRepository) : ViewModel() {
    var homeUiState: HomeUiState by mutableStateOf(HomeUiState.Loading)
        private set

    fun getStuffList(userId: Int) {
        viewModelScope.launch {
            homeUiState = HomeUiState.Loading
            try {
                // UPDATE: Pakai response.body() karena sekarang return Response<>
                val response = repository.getAllStuff(userId)
                if (response.isSuccessful) {
                    homeUiState = HomeUiState.Success(response.body() ?: emptyList())
                } else {
                    homeUiState = HomeUiState.Error("Gagal: ${response.message()}")
                }
            } catch (e: IOException) {
                homeUiState = HomeUiState.Error("Koneksi Error")
            } catch (e: Exception) {
                homeUiState = HomeUiState.Error("Error: ${e.message}")
            }
        }
    }
}