package com.example.savitapp.repository

import com.example.savitapp.model.LoginRequest
import com.example.savitapp.model.LoginResponse
import com.example.savitapp.model.RegisterRequest
import com.example.savitapp.model.RegisterResponse
import com.example.savitapp.service.ApiService
import retrofit2.Response

// Interface
interface AuthRepository {
    suspend fun register(request: RegisterRequest): Response<RegisterResponse>
    suspend fun login(request: LoginRequest): Response<LoginResponse>
}

// Class Implementasi (NetworkAuthRepository) <-- Ini yang dibutuhkan SavitApp
class NetworkAuthRepository(private val apiService: ApiService) : AuthRepository {
    override suspend fun register(request: RegisterRequest): Response<RegisterResponse> {
        return apiService.register(request)
    }

    override suspend fun login(request: LoginRequest): Response<LoginResponse> {
        return apiService.login(request)
    }
}