package com.example.savitapp.view.home

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.savitapp.model.InsertUiEvent
import com.example.savitapp.viewmodel.EntryViewModel
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
    val context = LocalContext.current // Butuh Context buat nampilin Toast

    // 1. Color Palette
    val HijauMuda = Color(0xFFA2B29F)
    val HijauTua = Color(0xFF798777)
    val Cream = Color(0xFFF8EDE3)
    val Hitam = Color(0xFF000000)
    val Putih = Color(0xFFFFFFFF)
    val Merah = Color(0xFFA00000)

    // --- STATE POP-UP ---
    var showConfirmationDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }

    // --- DIALOG KONFIRMASI ---
    if (showConfirmationDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmationDialog = false },
            containerColor = Cream,
            title = { Text("Konfirmasi", fontWeight = FontWeight.Bold, color = Hitam) },
            text = { Text("Apakah anda yakin ingin menambah barang ini?", color = Hitam) },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmationDialog = false

                        // CEK VALIDASI
                        if (viewModel.uiState.isEntryValid) {
                            // Jika Valid -> Simpan -> Toast Sukses -> Kembali
                            coroutineScope.launch {
                                viewModel.saveStuff(userId)
                                // --- INI YANG DITAMBAHKAN ---
                                Toast.makeText(context, "Barang berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
                                navigateBack()
                            }
                        } else {
                            // Jika Tidak Valid -> Muncul Dialog Error
                            showErrorDialog = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = HijauTua)
                ) {
                    Text("Ya, Simpan", color = Putih)
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmationDialog = false }) {
                    Text("Batal", color = Merah)
                }
            }
        )
    }

    // --- DIALOG ERROR (DATA TIDAK LENGKAP) ---
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            containerColor = Cream,
            title = { Text("Data Tidak Lengkap!", fontWeight = FontWeight.Bold, color = Merah) },
            text = { Text("Semua kolom (Nama, Harga, Hari, Prioritas) harus terisi semua! Harap cek kelengkapan data.", color = Hitam) },
            confirmButton = {
                Button(
                    onClick = { showErrorDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = HijauTua)
                ) {
                    Text("OK", color = Putih)
                }
            }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Tambah Barang",
                        fontWeight = FontWeight.Bold,
                        color = Hitam,
                        fontSize = 22.sp
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = HijauMuda
                )
            )
        },
        containerColor = Cream
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(24.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Form Input
            FormInput(
                insertUiEvent = viewModel.uiState.insertUiEvent,
                onValueChange = viewModel::updateUiState,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = HijauTua,
                    unfocusedIndicatorColor = HijauMuda,
                    focusedLabelColor = HijauTua,
                    unfocusedLabelColor = HijauTua,
                    cursorColor = Hitam
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Button Simpan
            Button(
                onClick = { showConfirmationDialog = true }, // Trigger Dialog Konfirmasi
                enabled = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp),
                shape = RoundedCornerShape(20),
                colors = ButtonDefaults.buttonColors(
                    containerColor = HijauTua,
                    contentColor = Putih
                )
            ) {
                Text("Simpan", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            // Button Kembali
            Button(
                onClick = navigateBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp),
                shape = RoundedCornerShape(20),
                colors = ButtonDefaults.buttonColors(containerColor = HijauMuda)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "Kembali",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        style = TextStyle.Default.copy(
                            drawStyle = Stroke(miter = 10f, width = 5f, join = StrokeJoin.Round)
                        ),
                        color = Putih
                    )
                    Text(
                        text = "Kembali",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Merah
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormInput(
    insertUiEvent: InsertUiEvent,
    onValueChange: (InsertUiEvent) -> Unit,
    modifier: Modifier = Modifier,
    colors: TextFieldColors
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {

        // Nama Barang
        OutlinedTextField(
            value = insertUiEvent.namaBarang,
            onValueChange = { onValueChange(insertUiEvent.copy(namaBarang = it)) },
            label = { Text("Nama Barang") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = colors,
            singleLine = true
        )

        // Rencana Hari
        OutlinedTextField(
            value = insertUiEvent.rencanaHari,
            onValueChange = { onValueChange(insertUiEvent.copy(rencanaHari = it)) },
            label = { Text("Rencana Hari (cth: 30)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = colors,
            singleLine = true
        )

        // Dropdown Prioritas
        var expanded by remember { mutableStateOf(false) }
        val options = listOf("Penting", "Sedang", "Rendah")

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = insertUiEvent.prioritas,
                onValueChange = {},
                readOnly = true,
                label = { Text("Skala Prioritas") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.fillMaxWidth().menuAnchor(),
                shape = RoundedCornerShape(12.dp),
                colors = colors
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onValueChange(insertUiEvent.copy(prioritas = option))
                            expanded = false
                        }
                    )
                }
            }
        }

        // Harga Barang
        OutlinedTextField(
            value = insertUiEvent.hargaBarang,
            onValueChange = { onValueChange(insertUiEvent.copy(hargaBarang = it)) },
            label = { Text("Harga Barang (Rp)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = colors,
            singleLine = true
        )
    }
}