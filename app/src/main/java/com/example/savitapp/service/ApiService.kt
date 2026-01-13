package com.example.savitapp.service

import com.example.savitapp.model.TransactionRequest
import com.example.savitapp.model.GeneralResponse
import com.example.savitapp.model.Stuff
import com.example.savitapp.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.DELETE
import retrofit2.http.PUT


interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @GET("stuff/{user_id}")
    suspend fun getAllStuff(@Path("user_id") userId: Int): Response<List<Stuff>>

    @POST("stuff")
    suspend fun createStuff(@Body stuff: Stuff): Response<GeneralResponse>

    @POST("transaction")
    suspend fun addTransaction(@Body request: TransactionRequest): Response<GeneralResponse>

    @PUT("stuff/{id}")
    suspend fun updateStuff(@Path("id") id: Int, @Body stuff: Stuff): Response<GeneralResponse>

    @DELETE("stuff/{id}")
    suspend fun deleteStuff(@Path("id") id: Int): Response<GeneralResponse>

    @GET("transaction/history/{stuff_id}")
    suspend fun getHistory(@Path("stuff_id") stuffId: Int): Response<List<History>>
}