package com.example.savitapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.savitapp.model.Stuff
import com.example.savitapp.repository.StuffRepository
import kotlinx.coroutines.launch

class EntryViewModel(private val repository: StuffRepository) : ViewModel() {

    // State untuk form input
    var uiState: EntryUiState by mutableStateOf(EntryUiState())
        private set

    // Fungsi untuk update state saat user mengetik
    fun updateUiState(event: InsertUiEvent) {
        uiState = EntryUiState(
            insertUiEvent = event,
            isEntryValid = validasiInput(event)
        )
    }

    // Validasi sederhana (sesuai SRS: tidak boleh kosong & angka > 0)
    private fun validasiInput(event: InsertUiEvent = uiState.insertUiEvent): Boolean {
        return event.namaBarang.isNotBlank() &&
                event.hargaBarang.isNotBlank() && (event.hargaBarang.toLongOrNull() ?: 0) > 0 &&
                event.rencanaHari.isNotBlank() && (event.rencanaHari.toIntOrNull() ?: 0) > 0 &&
                event.prioritas.isNotBlank()
    }

    // Fungsi Simpan Data ke Backend
    fun saveStuff(userId: Int) {
        viewModelScope.launch {
            try {
                if (validasiInput()) {
                    repository.insertStuff(uiState.insertUiEvent.toStuff(userId))
                    // Reset form setelah sukses (opsional)
                    // uiState = EntryUiState()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

// --- Data Classes untuk State UI ---

data class EntryUiState(
    val insertUiEvent: InsertUiEvent = InsertUiEvent(),
    val isEntryValid: Boolean = false
)

data class InsertUiEvent(
    val namaBarang: String = "",
    val hargaBarang: String = "",
    val rencanaHari: String = "",
    val prioritas: String = "Penting" // Default selection
)

// Extension function: Konversi dari UI Event ke Model Data (Stuff)
fun InsertUiEvent.toStuff(userId: Int): Stuff = Stuff(
    userId = userId,
    namaBarang = namaBarang,
    hargaBarang = hargaBarang.toLongOrNull() ?: 0,
    rencanaHari = rencanaHari.toIntOrNull() ?: 0,
    uangTerkumpul = 0, // Awal buat pasti 0
    prioritas = prioritas
)