package com.example.savitapp.view.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.savitapp.model.Stuff
import com.example.savitapp.viewmodel.HomeUiState
import com.example.savitapp.viewmodel.HomeViewModel
import com.example.savitapp.viewmodel.PenyediaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    userId: Int,
    onNavigateToAdd: () -> Unit,
    onDetailClick: (Int) -> Unit,
    viewModel: HomeViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    // State untuk menyimpan barang yang sedang dipilih untuk Quick Add
    // Jika null = dialog tertutup. Jika berisi Stuff = dialog terbuka.
    var selectedStuffForQuickAdd by remember { mutableStateOf<Stuff?>(null) }

    // Load data saat halaman pertama kali dibuka atau userId berubah
    LaunchedEffect(userId) {
        viewModel.getStuffList(userId)
    }

    Scaffold(
        topBar = {
            // Custom Header mirip desain "Halo, User"
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFA3B8A3)) // Warna Hijau Header
                    .padding(20.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(24.dp))

                        Text(
                            text = "Halo, User", // Nanti bisa ambil nama dari SharedPreferences
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Text(
                        text = "Dashboard",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Black,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 10.dp)
                    )
                }
            }
        },
        floatingActionButton = {
            // Tombol Tambah Barang (FAB Besar di bawah kanan)
            FloatingActionButton(
                onClick = onNavigateToAdd,
                containerColor = Color(0xFF7D9581),
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.size(60.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Tambah Barang",
                    modifier = Modifier.size(30.dp)
                )
            }
        },
        containerColor = Color(0xFFF5EFE6) // Background Krem
    ) { innerPadding ->

        // Menangani Status Loading/Error/Success
        when (val state = viewModel.homeUiState) {
            is HomeUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF1B3B28))
                }
            }
            is HomeUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = state.message,
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
            is HomeUiState.Success -> {
                HomeBody(
                    stuffList = state.stuffList,
                    onItemClick = onDetailClick,
                    onQuickAddClick = { stuff ->
                        // Saat tombol + kecil diklik, simpan data barang ke state agar dialog muncul
                        selectedStuffForQuickAdd = stuff
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }

        // LOGIKA DIALOG QUICK ADD
        // Jika variabel selectedStuffForQuickAdd TIDAK null, tampilkan Dialog
        if (selectedStuffForQuickAdd != null) {
            QuickAddDialog(
                stuff = selectedStuffForQuickAdd!!,
                onDismiss = { selectedStuffForQuickAdd = null },
                onSuccess = {
                    selectedStuffForQuickAdd = null
                    // Refresh data dashboard setelah saldo bertambah
                    viewModel.getStuffList(userId)
                }
            )
        }
    }
}

@Composable
fun HomeBody(
    stuffList: List<Stuff>,
    onItemClick: (Int) -> Unit,
    onQuickAddClick: (Stuff) -> Unit,
    modifier: Modifier = Modifier
) {
    if (stuffList.isEmpty()) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Belum ada target. Yuk tambah!", color = Color.Gray)
        }
    } else {
        LazyColumn(
            modifier = modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(stuffList) { stuff ->
                StuffCard(
                    stuff = stuff,
                    onClick = { onItemClick(stuff.stuffId) },
                    onQuickAddClick = { onQuickAddClick(stuff) }
                )
            }
        }
    }
}

@Composable
fun StuffCard(
    stuff: Stuff,
    onClick: () -> Unit,
    onQuickAddClick: () -> Unit
) {
    // Logika Warna Prioritas
    val cardColor = when (stuff.prioritas) {
        "Penting" -> Color(0xFFA3B8A3) // Hijau Sage
        "Sedang" -> Color(0xFFFFF59D)  // Kuning
        "Rendah" -> Color(0xFFFFCC80)  // Orange
        else -> Color(0xFFA3B8A3)
    }

    // Hitung Progress (Mencegah pembagian dengan nol)
    val progress = if (stuff.hargaBarang > 0) {
        stuff.uangTerkumpul.toFloat() / stuff.hargaBarang.toFloat()
    } else 0f

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stuff.namaBarang,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                // TOMBOL QUICK ADD (+) KECIL
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color(0xFFF5EFE6), shape = RoundedCornerShape(8.dp))
                        .clickable { onQuickAddClick() }, // Klik tombol ini trigger dialog
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Quick Add",
                        tint = Color.Black
                    )
                }
            }

            Text(
                text = "Hari tersisa : ${stuff.rencanaHari} hari",
                fontSize = 14.sp,
                color = Color.Black,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Progress Bar Putih
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = Color.White,
                trackColor = Color.Black.copy(alpha = 0.2f)
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Teks nominal di bawah progress bar
            Text(
                text = "Rp ${stuff.uangTerkumpul} / Rp ${stuff.hargaBarang}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}