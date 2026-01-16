package com.example.savitapp.view.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
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

    // 1. Color Palette (Konsisten)
    val HijauMuda = Color(0xFFA2B29F)
    val HijauTua = Color(0xFF798777)
    val Cream = Color(0xFFF8EDE3)
    val Hitam = Color(0xFF000000)
    val Putih = Color(0xFFFFFFFF)
    val Merah = Color(0xFFFF0000)

    // Load data barang saat halaman dibuka agar form terisi data lama
    LaunchedEffect(Unit) {
        viewModel.loadStuffData(userId, stuffId)
    }

    Scaffold(
        topBar = {
            // 2. TopAppBar Hijau Muda, Tulisan "Edit barang" Hitam Bold
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
        containerColor = Cream // Background Cream
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(24.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 3. Panggil FormInput (Style sama persis dengan halaman Tambah)
            // Pastikan fungsi 'FormInput' di EntryStuffScreen.kt sudah PUBLIC (tidak private)
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

            // 4. Button "Update barang" (Hijau Tua, Teks Putih)
            Button(
                onClick = {
                    coroutineScope.launch {
                        viewModel.updateStuff(userId, stuffId) {
                            navigateBack()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(50), // Rounded Edge
                colors = ButtonDefaults.buttonColors(containerColor = HijauTua)
            ) {
                Text(
                    text = "Update barang",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Putih
                )
            }

            // 5. Button "Kembali" (Hijau Muda, Teks Merah Outline Putih)
            Button(
                onClick = navigateBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(50), // Rounded Edge
                colors = ButtonDefaults.buttonColors(containerColor = HijauMuda)
            ) {
                // Trik Outline Text
                Box(contentAlignment = Alignment.Center) {
                    // Layer 1: Stroke Putih
                    Text(
                        text = "Kembali",
                        fontSize = 18.sp,
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
                    // Layer 2: Fill Merah
                    Text(
                        text = "Kembali",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Merah
                    )
                }
            }
        }
    }
}