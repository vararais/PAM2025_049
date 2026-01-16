package com.example.savitapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.savitapp.model.EntryUiState
import com.example.savitapp.model.InsertUiEvent
import com.example.savitapp.model.Stuff // <--- Pastikan ini ada
import com.example.savitapp.model.toStuff
import com.example.savitapp.model.toUiStateEntry // <--- WAJIB IMPORT INI
import com.example.savitapp.repository.StuffRepository
import kotlinx.coroutines.launch

class EditViewModel(private val repository: StuffRepository) : ViewModel() {
    var uiState by mutableStateOf(EntryUiState())
        private set

    fun updateUiState(event: InsertUiEvent) {
        uiState = EntryUiState(insertUiEvent = event, isEntryValid = validateInput(event))
    }

    private fun validateInput(event: InsertUiEvent = uiState.insertUiEvent): Boolean {
        return event.namaBarang.isNotBlank() && event.hargaBarang.isNotBlank() && event.rencanaHari.isNotBlank()
    }

    fun loadStuffData(userId: Int, stuffId: Int) {
        viewModelScope.launch {
            try {
                // TRIK: Panggil getAllStuff (Jalur Dashboard yang Aman)
                val response = repository.getAllStuff(userId)

                if (response.isSuccessful) {
                    val allStuff = response.body()
                    // Cari barang yang ID-nya sama
                    val targetStuff = allStuff?.find { it.stuffId == stuffId }

                    if (targetStuff != null) {
                        // Masukkan ke Form
                        uiState = targetStuff.toUiStateEntry()
                    }
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun updateStuff(userId: Int, stuffId: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            if (validateInput()) {
                val stuff = uiState.insertUiEvent.toStuff()
                val stuffToUpdate = stuff.copy(stuffId = stuffId, userId = userId)
                try {
                    val response = repository.updateStuff(stuffId, stuffToUpdate)
                    if (response.isSuccessful) { onSuccess() }
                } catch (e: Exception) { }
            }
        }
    }
}