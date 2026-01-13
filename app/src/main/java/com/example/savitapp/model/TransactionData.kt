package com.example.savitapp.model

import com.google.gson.annotations.SerializedName

data class TransactionRequest(
    @SerializedName("stuff_id") val stuffId: Int,
    val nominal: Long
)