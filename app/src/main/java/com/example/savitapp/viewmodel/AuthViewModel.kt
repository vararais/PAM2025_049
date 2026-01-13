package com.example.savitapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.savitapp.model.LoginRequest
import com.example.savitapp.model.RegisterRequest
import com.example.savitapp.repository.AuthRepository
import kotlinx.coroutines.launch
import java.io.IOException

// State UI untuk memberi tahu loading/sukses/gagal
sealed interface AuthUiState {
    object Idle : AuthUiState
    object Loading : AuthUiState
    data class Success(val message: String, val userId: Int? = null, val nama: String? = null) : AuthUiState
    data class Error(val message: String) : AuthUiState
}

class LoginViewModel(private val repository: AuthRepository) : ViewModel() {
    var uiState: AuthUiState by mutableStateOf(AuthUiState.Idle)
        private set

    // Fungsi Login
    fun login(email: String, pass: String) {
        uiState = AuthUiState.Loading
        viewModelScope.launch {
            try {
                val response = repository.login(LoginRequest(email, pass))
                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    uiState = AuthUiState.Success(body.message, body.userId, body.nama)
                } else {
                    uiState = AuthUiState.Error("Login Gagal: Cek Email/Password")
                }
            } catch (e: IOException) {
                uiState = AuthUiState.Error("Koneksi Error: ${e.message}")
            }
        }
    }

    fun resetState() { uiState = AuthUiState.Idle }
}

class RegisterViewModel(private val repository: AuthRepository) : ViewModel() {
    var uiState: AuthUiState by mutableStateOf(AuthUiState.Idle)
        private set

    fun register(nama: String, email: String, pass: String) {
        uiState = AuthUiState.Loading
        viewModelScope.launch {
            try {
                val response = repository.register(RegisterRequest(nama, email, pass))
                if (response.isSuccessful) {
                    uiState = AuthUiState.Success("Registrasi Berhasil! Silakan Login")
                } else {
                    uiState = AuthUiState.Error("Gagal: Email mungkin sudah dipakai")
                }
            } catch (e: Exception) {
                uiState = AuthUiState.Error("Error: ${e.message}")
            }
        }
    }
}