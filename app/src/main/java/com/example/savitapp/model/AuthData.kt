package com.example.savitapp.model

import com.google.gson.annotations.SerializedName

data class LoginRequest(val email: String, val password: String)

data class LoginResponse(
    val message: String,
    @SerializedName("user_id") val userId: Int?,
    val nama: String?
)

data class RegisterRequest(val nama: String, val email: String, val password: String)
data class RegisterResponse(val message: String)