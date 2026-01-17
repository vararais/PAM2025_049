package com.example.savitapp.view.home

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.savitapp.viewmodel.EditViewModel
import com.example.savitapp.viewmodel.PenyediaViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditStuffScreen(
    userId: Int,
    stuffId: Int,
    navigateBack: () -> Unit,
    viewModel: EditViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val context = LocalContext.current // Butuh Context buat Toast

    // 1. Color Palette
    val HijauMuda = Color(0xFFA2B29F)
    val HijauTua = Color(0xFF798777)
    val Cream = Color(0xFFF8EDE3)
    val Hitam = Color(0xFF000000)
    val Putih = Color(0xFFFFFFFF)
    val Merah = Color(0xFFA00000)

    // --- STATE UNTUK POP-UP (DIALOG) ---
    var showConfirmationDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }

    // Load data lama
    LaunchedEffect(Unit) {
        viewModel.loadStuffData(userId, stuffId)
    }

    // --- DIALOG KONFIRMASI (Yakin Update?) ---
    if (showConfirmationDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmationDialog = false },
            containerColor = Cream,
            title = { Text("Konfirmasi Update", fontWeight = FontWeight.Bold, color = Hitam) },
            text = { Text("Apakah anda yakin ingin mengubah data barang ini?", color = Hitam) },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmationDialog = false

                        // --- CEK VALIDASI ---
                        if (viewModel.uiState.isEntryValid) {
                            // Jika Valid -> Update -> Toast -> Kembali
                            coroutineScope.launch {
                                viewModel.updateStuff(userId, stuffId) {
                                    Toast.makeText(context, "Barang berhasil di update!", Toast.LENGTH_SHORT).show()
                                    navigateBack()
                                }
                            }
                        } else {
                            // Jika TIDAK Valid -> Muncul Error
                            showErrorDialog = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = HijauTua)
                ) {
                    Text("Ya, Update", color = Putih)
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmationDialog = false }) {
                    Text("Batal", color = Merah)
                }
            }
        )
    }

    // --- DIALOG ERROR (Data Tidak Lengkap) ---
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            containerColor = Cream,
            title = { Text("Data Tidak Lengkap!", fontWeight = FontWeight.Bold, color = Merah) },
            text = { Text("Semua kolom harus terisi valid! Harap cek kelengkapan update data.", color = Hitam) },
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
                        "Edit barang",
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
            modifier = Modifier
                .padding(innerPadding)
                .padding(24.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 3. Panggil FormInput (Style sama persis dengan halaman Tambah)
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

            // 4. Button "Update barang"
            Button(
                onClick = {
                    // Trigger Dialog Konfirmasi dulu
                    showConfirmationDialog = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                shape = RoundedCornerShape(20), // Disamakan dengan EntryScreen (50)
                colors = ButtonDefaults.buttonColors(containerColor = HijauTua)
            ) {
                Text(
                    text = "Update barang",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = Putih
                )
            }

            // 5. Button "Kembali"
            Button(
                onClick = navigateBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                shape = RoundedCornerShape(20), // Disamakan dengan EntryScreen (50)
                colors = ButtonDefaults.buttonColors(containerColor = HijauMuda)
            ) {
                // Trik Outline Text
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "Kembali",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        style = TextStyle.Default.copy(
                            drawStyle = Stroke(
                                miter = 10f,
                                width = 5f,
                                join = StrokeJoin.Round
                            )
                        ),
                        color = Putih
                    )
                    Text(
                        text = "Kembali",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = Merah
                    )
                }
            }
        }
    }
}