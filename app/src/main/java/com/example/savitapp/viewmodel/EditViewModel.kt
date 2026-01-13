package com.example.savitapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.savitapp.repository.StuffRepository
import kotlinx.coroutines.launch

class EditViewModel(private val repository: StuffRepository) : ViewModel() {
    var uiState: EntryUiState by mutableStateOf(EntryUiState())
        private set

    // Load data awal untuk diedit
    fun loadStuffData(userId: Int, stuffId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getAllStuff(userId)
                val stuff = response.body()?.find { it.stuffId == stuffId }
                if (stuff != null) {
                    uiState = EntryUiState(
                        insertUiEvent = InsertUiEvent(
                            namaBarang = stuff.namaBarang,
                            hargaBarang = stuff.hargaBarang.toString(),
                            rencanaHari = stuff.rencanaHari.toString(),
                            prioritas = stuff.prioritas
                        ),
                        isEntryValid = true
                    )
                }
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    fun updateUiState(event: InsertUiEvent) {
        uiState = EntryUiState(insertUiEvent = event, isEntryValid = true) // Validasi simpel
    }

    fun updateStuff(userId: Int, stuffId: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val stuff = uiState.insertUiEvent.toStuff(userId)
                val response = repository.updateStuff(stuffId, stuff)
                if (response.isSuccessful) {
                    onSuccess()
                }
            } catch (e: Exception) { e.printStackTrace() }
        }
    }
}