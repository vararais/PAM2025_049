package com.example.savitapp.model

import com.google.gson.annotations.SerializedName

// 1. Model Utama (Jangan diubah, ini buat Database)
data class Stuff(
    @SerializedName("stuff_id") val stuffId: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("nama_barang") val namaBarang: String,
    @SerializedName("rencana_hari") val rencanaHari: Int,
    @SerializedName("prioritas") val prioritas: String,
    @SerializedName("harga_barang") val hargaBarang: Long,
    @SerializedName("uang_terkumpul") val uangTerkumpul: Long
)

// 2. Model Tambahan (INI YANG HILANG DAN BIKIN ERROR MERAH)
// UI butuh ini untuk menampung ketikan user di form input/edit
data class InsertUiEvent(
    val namaBarang: String = "",
    val rencanaHari: String = "",
    val prioritas: String = "Penting",
    val hargaBarang: String = ""
)

// Wadah status UI (Valid atau Tidak)
data class EntryUiState(
    val insertUiEvent: InsertUiEvent = InsertUiEvent(),
    val isEntryValid: Boolean = false
)

// 3. Fungsi Bantuan (Konversi Data)
// Mengubah data dari Database ke Form UI (untuk Edit)
fun Stuff.toUiStateEntry(): EntryUiState = EntryUiState(
    insertUiEvent = InsertUiEvent(
        namaBarang = namaBarang,
        rencanaHari = rencanaHari.toString(),
        prioritas = prioritas,
        hargaBarang = hargaBarang.toString()
    )
)

// Mengubah data dari Form UI ke Database (untuk Simpan)
fun InsertUiEvent.toStuff(): Stuff = Stuff(
    stuffId = 0,
    userId = 0, // Nanti diisi otomatis
    namaBarang = namaBarang,
    rencanaHari = rencanaHari.toIntOrNull() ?: 0,
    prioritas = prioritas,
    hargaBarang = hargaBarang.toLongOrNull() ?: 0,
    uangTerkumpul = 0
)