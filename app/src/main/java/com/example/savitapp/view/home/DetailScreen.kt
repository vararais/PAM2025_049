package com.example.savitapp.view.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.savitapp.model.Stuff
import com.example.savitapp.viewmodel.DetailUiState
import com.example.savitapp.viewmodel.DetailViewModel
import com.example.savitapp.viewmodel.PenyediaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    userId: Int,
    stuffId: Int,
    navigateBack: () -> Unit,
    onEditClick: (Int) -> Unit,
    viewModel: DetailViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    // 1. Color Palette
    val HijauMuda = Color(0xFFA2B29F)
    val HijauTua = Color(0xFF798777)
    val Cream = Color(0xFFF8EDE3)
    val Hitam = Color(0xFF000000)
    val Putih = Color(0xFFFFFFFF)
    val Merah = Color(0xFFFF0000)

    LaunchedEffect(Unit) {
        viewModel.getStuffDetail(userId, stuffId)
    }

    // State untuk Dialog Konfirmasi Hapus
    val showDeleteDialog = remember { mutableStateOf(false) }

    if (showDeleteDialog.value) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog.value = false },
            title = { Text("Hapus Barang?") },
            text = { Text("Barang ini akan dihapus permanen.") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteStuff(stuffId) {
                            showDeleteDialog.value = false
                            navigateBack()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Merah)
                ) { Text("Hapus", color = Putih) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog.value = false }) { Text("Batal", color = Hitam) }
            }
        )
    }

    Scaffold(
        containerColor = Cream // Background Cream
    ) { innerPadding ->

        val scrollState = rememberScrollState()

        when (val state = viewModel.detailUiState) {
            is DetailUiState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = HijauTua) }
            }
            is DetailUiState.Error -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(state.message, color = Merah) }
            }
            is DetailUiState.Success -> {
                val stuff = state.stuff

                // Hitung Persentase
                val percentage = if (stuff.hargaBarang > 0) {
                    ((stuff.uangTerkumpul.toDouble() / stuff.hargaBarang.toDouble()) * 100).toInt()
                } else 0
                val progressFloat = if (stuff.hargaBarang > 0) {
                    stuff.uangTerkumpul.toFloat() / stuff.hargaBarang.toFloat()
                } else 0f

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(scrollState), // Agar bisa di-scroll
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 2. Shape Kotak Setengah Atas (Hijau Muda)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(320.dp) // Tinggi area atas
                            .clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp)) // Rounded bawah
                            .background(HijauMuda),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Text(
                                text = "Hore , Progress Kamu sudah $percentage%",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Hitam,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            // Linear Progress Indicator
                            LinearProgressIndicator(
                                progress = progressFloat,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(20.dp)
                                    .clip(RoundedCornerShape(10.dp)),
                                color = HijauTua, // Warna Bar
                                trackColor = Putih // Warna Track
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Dana terkumpul",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = Hitam
                            )
                            Text(
                                text = "Rp ${stuff.uangTerkumpul}",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Hitam
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // 3. Button "Edit barang" (Hijau Tua, Rounded, Icon Edit)
                    Button(
                        onClick = { onEditClick(stuffId) },
                        modifier = Modifier
                            .fillMaxWidth(0.8f) // Lebar 80% dari layar
                            .height(50.dp),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = HijauTua)
                    ) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = null, tint = Putih)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Edit barang", color = Putih, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // 4. Button "Hapus" (Merah, Rounded, Icon Hapus)
                    Button(
                        onClick = { showDeleteDialog.value = true },
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(50.dp),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = Merah)
                    ) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = null, tint = Putih)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Hapus", color = Putih, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // 5. Textbox Statis (Hijau Muda, Tulisan Hitam)
                    // Kita pakai Column container untuk menampung info
                    Column(
                        modifier = Modifier.fillMaxWidth(0.9f), // Lebar 90%
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StaticInfoBox("Nama Barang", stuff.namaBarang, HijauMuda, Hitam)
                        StaticInfoBox("Skala Prioritas", stuff.prioritas, HijauMuda, Hitam)
                        StaticInfoBox("Harga Barang", "Rp ${stuff.hargaBarang}", HijauMuda, Hitam)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // 6. Button Kembali (Hijau Tua, Merah Outline Putih)
                    Button(
                        onClick = navigateBack,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(50.dp),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = HijauTua)
                    ) {
                        // Trik Outline Text
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "Kembali",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                style = TextStyle.Default.copy(
                                    drawStyle = Stroke(
                                        miter = 10f,
                                        width = 5f,
                                        join = StrokeJoin.Round
                                    )
                                ),
                                color = Putih // Outline Putih
                            )
                            Text(
                                text = "Kembali",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Merah // Isi Merah
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(30.dp)) // Jarak bawah agar tidak mepet
                }
            }
        }
    }
}

// Komponen Custom untuk Textbox Statis
@Composable
fun StaticInfoBox(label: String, value: String, bgColor: Color, textColor: Color) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(start = 12.dp, bottom = 4.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(bgColor, RoundedCornerShape(12.dp)) // Background Hijau Muda
                .padding(16.dp)
        ) {
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = textColor // Teks Hitam
            )
        }
    }
}