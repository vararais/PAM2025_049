package com.example.savitapp.model

import com.google.gson.annotations.SerializedName

data class History(
    @SerializedName("history_id") val historyId: Int,
    @SerializedName("stuff_id") val stuffId: Int,
    val nominal: Long,
    val tanggal: String // Format YYYY-MM-DD dari backend
)