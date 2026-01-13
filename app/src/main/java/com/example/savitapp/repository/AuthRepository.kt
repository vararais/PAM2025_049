package com.example.savitapp.repository

import com.example.savitapp.model.*
import com.example.savitapp.service.ApiService
import retrofit2.Response

interface AuthRepository {
    suspend fun login(request: LoginRequest): Response<LoginResponse>
    suspend fun register(request: RegisterRequest): Response<RegisterResponse>
}

class NetworkAuthRepository(private val apiService: ApiService) : AuthRepository {
    override suspend fun login(request: LoginRequest) = apiService.login(request)
    override suspend fun register(request: RegisterRequest) = apiService.register(request)
}