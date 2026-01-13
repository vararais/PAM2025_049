package com.example.savitapp.view.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.savitapp.model.Stuff
import com.example.savitapp.viewmodel.PenyediaViewModel
import com.example.savitapp.viewmodel.QuickAddViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickAddDialog(
    stuff: Stuff,
    onDismiss: () -> Unit,
    onSuccess: () -> Unit,
    viewModel: QuickAddViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    // Rumus Matematika (SRS BRG-02)
    // Target Harian = (Harga - Terkumpul) / Sisa Hari
    val kekurangan = if (stuff.hargaBarang > stuff.uangTerkumpul) stuff.hargaBarang - stuff.uangTerkumpul else 0
    val sisaHari = if (stuff.rencanaHari > 0) stuff.rencanaHari else 1
    val targetHarian = kekurangan / sisaHari

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5EFE6)), // Cream
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header Hijau
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFA3B8A3), shape = RoundedCornerShape(8.dp))
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Quick Add",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                // Info Target Harian
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Masukkan Saldo", fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Target Per Hari: Rp $targetHarian",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                // Input Nominal
                OutlinedTextField(
                    value = viewModel.nominalInput,
                    onValueChange = { viewModel.updateNominal(it) },
                    label = { Text("Contoh: 100000") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )

                // Tombol Action
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = {
                            viewModel.saveTransaction(stuff.stuffId) {
                                onSuccess()
                                onDismiss()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7D9581))
                    ) {
                        Text("Selesai", color = Color.White)
                    }

                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
                    ) {
                        Text("Kembali")
                    }
                }
            }
        }
    }
}