package com.example.savitapp.view.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.savitapp.model.History
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
    LaunchedEffect(Unit) {
        viewModel.getStuffDetail(userId, stuffId)
    }

    // Dialog Konfirmasi Hapus
    val showDeleteDialog = remember { mutableStateOf(false) }

    if (showDeleteDialog.value) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog.value = false },
            title = { Text("Hapus Barang?") },
            text = { Text("Barang ini dan semua riwayatnya akan dihapus permanen.") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteStuff(stuffId) {
                            showDeleteDialog.value = false
                            navigateBack()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) { Text("Hapus") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog.value = false }) { Text("Batal") }
            }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Detail Barang") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { onEditClick(stuffId) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = { showDeleteDialog.value = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color(0xFFA3B8A3))
            )
        },
        containerColor = Color(0xFFF5EFE6)
    ) { innerPadding ->
        when (val state = viewModel.detailUiState) {
            is DetailUiState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            is DetailUiState.Error -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(state.message) }
            is DetailUiState.Success -> {
                Column(modifier = Modifier.padding(innerPadding).padding(16.dp)) {
                    // Bagian Atas: Detail Stuff
                    DetailInfoCard(state.stuff)

                    Spacer(modifier = Modifier.height(20.dp))

                    Text("Riwayat Transaksi", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))

                    // Bagian Bawah: List History
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(state.historyList) { history ->
                            HistoryItem(history)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailInfoCard(stuff: Stuff) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(stuff.namaBarang, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text("Prioritas: ${stuff.prioritas}", color = Color.Gray)
            Spacer(modifier = Modifier.height(10.dp))

            // Progress Calculation
            val progress = if (stuff.hargaBarang > 0) stuff.uangTerkumpul.toFloat() / stuff.hargaBarang.toFloat() else 0f
            LinearProgressIndicator(progress = progress, modifier = Modifier.fillMaxWidth().height(10.dp).background(Color.LightGray, RoundedCornerShape(5.dp)))

            Spacer(modifier = Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Terkumpul: Rp ${stuff.uangTerkumpul}", fontWeight = FontWeight.Medium)
                Text("Target: Rp ${stuff.hargaBarang}", fontWeight = FontWeight.Medium)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("Sisa Waktu: ${stuff.rencanaHari} Hari", color = Color(0xFF7D9581), fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun HistoryItem(history: History) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9C4)) // Kuning muda
    ) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = history.tanggal.split("T")[0]) // Ambil tanggalnya saja
            Text(text = "+ Rp ${history.nominal}", fontWeight = FontWeight.Bold, color = Color(0xFF388E3C))
        }
    }
}