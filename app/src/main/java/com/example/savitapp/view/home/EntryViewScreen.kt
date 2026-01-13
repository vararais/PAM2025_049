package com.example.savitapp.view.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.savitapp.viewmodel.EntryViewModel
import com.example.savitapp.viewmodel.InsertUiEvent
import com.example.savitapp.viewmodel.PenyediaViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryStuffScreen(
    userId: Int,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EntryViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Form Barang") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFA3B8A3) // Hijau Header
                )
            )
        },
        containerColor = Color(0xFFF5EFE6) // Cream Background
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FormInput(
                insertUiEvent = viewModel.uiState.insertUiEvent,
                onValueChange = viewModel::updateUiState,
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    coroutineScope.launch {
                        viewModel.saveStuff(userId)
                        navigateBack() // Kembali ke dashboard setelah simpan
                    }
                },
                enabled = viewModel.uiState.isEntryValid,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7D9581) // Hijau Tombol
                )
            ) {
                Text("Simpan")
            }

            OutlinedButton(
                onClick = navigateBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Kembali", color = Color.Black)
            }
        }
    }
}

@Composable
fun FormInput(
    insertUiEvent: InsertUiEvent,
    onValueChange: (InsertUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {

        OutlinedTextField(
            value = insertUiEvent.namaBarang,
            onValueChange = { onValueChange(insertUiEvent.copy(namaBarang = it)) },
            label = { Text("Nama Barang") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = insertUiEvent.rencanaHari,
            onValueChange = { onValueChange(insertUiEvent.copy(rencanaHari = it)) },
            label = { Text("Rencana Hari (cth: 30)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = insertUiEvent.hargaBarang,
            onValueChange = { onValueChange(insertUiEvent.copy(hargaBarang = it)) },
            label = { Text("Harga Barang (Rp)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Text(text = "Skala Prioritas", style = MaterialTheme.typography.titleMedium)

        // Radio Button untuk Prioritas
        val prioritasOptions = listOf("Penting", "Sedang", "Rendah")
        Column {
            prioritasOptions.forEach { text ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = (text == insertUiEvent.prioritas),
                            onClick = { onValueChange(insertUiEvent.copy(prioritas = text)) }
                        )
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (text == insertUiEvent.prioritas),
                        onClick = { onValueChange(insertUiEvent.copy(prioritas = text)) }
                    )
                    Text(text = text, modifier = Modifier.padding(start = 8.dp))
                }
            }
        }
    }
}