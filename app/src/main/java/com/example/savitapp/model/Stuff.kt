package com.example.savitapp.model

import com.google.gson.annotations.SerializedName

data class Stuff(
    @SerializedName("stuff_id") val stuffId: Int = 0,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("nama_barang") val namaBarang: String,
    @SerializedName("harga_barang") val hargaBarang: Long,
    @SerializedName("rencana_hari") val rencanaHari: Int,
    @SerializedName("uang_terkumpul") val uangTerkumpul: Long,
    val prioritas: String
)