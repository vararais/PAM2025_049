package com.example.savitapp.view.home

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.savitapp.R
import androidx.compose.material.icons.filled.List

@Composable
fun ProfileScreen(
    onNavigateToHistory: () -> Unit,
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit
) {
    // 1. Color Palette
    val HijauMuda = Color(0xFFA2B29F)
    val HijauTua = Color(0xFF798777)
    val Cream = Color(0xFFF8EDE3)
    val Hitam = Color(0xFF000000)
    val Putih = Color(0xFFFFFFFF)
    val Merah = Color(0xFFA00000)

    // State untuk Pop-up Dialog Logout
    var showLogoutDialog by remember { mutableStateOf(false) }

    // Ambil Nama User (Opsional, biar profilnya ada namanya)
    val context = LocalContext.current
    val sharedPreferences = remember {
        context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)
    }
    val namaUser = sharedPreferences.getString("NAMA_USER", "User") ?: "User"

    // Logic Pop-up Logout
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text(text = "Konfirmasi Logout", fontWeight = FontWeight.Bold) },
            text = { Text("Apakah Anda yakin ingin keluar dari aplikasi?") },
            containerColor = Cream, // Biar senada
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        onLogout() // Panggil fungsi logout navigasi
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Merah)
                ) {
                    Text("Ya, Keluar", color = Putih, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Batal", color = Hitam)
                }
            }
        )
    }

    Scaffold(
        containerColor = Cream // Background Cream
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center // Isi konten di tengah layar
        ) {

            // 2. Profile Picture (Circle Shape)
            // PENTING: Ganti R.drawable.profile_pic dengan nama gambar profil kamu
            // Kalau belum ada gambar, bisa pakai R.drawable.stars sementara atau icon default
            Image(
                painter = painterResource(id = R.drawable.hiu), // <-- GANTI INI SESUAI GAMBARMU
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(250.dp) // Ukuran besar
                    .clip(CircleShape) // Bentuk Lingkaran
                    .border(4.dp, HijauTua, CircleShape) // Border Hijau Tua biar rapi
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Nama User di bawah foto
            Text(
                text = namaUser,
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold,
                color = Hitam
            )

            Spacer(modifier = Modifier.height(40.dp))

            // 3. Button "Lihat Riwayat" (Hijau Muda, Rounded, Icon Jam Hitam Bold)
            Button(
                onClick = onNavigateToHistory,
                modifier = Modifier
                    .fillMaxWidth(0.8f) // Lebar 80%
                    .height(70.dp),
                shape = RoundedCornerShape(20),
                colors = ButtonDefaults.buttonColors(containerColor = HijauMuda)
            ) {
                Icon(
                    imageVector = Icons.Default.List, // Icon Jam/History
                    contentDescription = null,
                    tint = Hitam,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Lihat Riwayat",
                    color = Hitam,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 4. Button "Kembali" (Hijau Tua, Rounded, Teks Merah Outline Putih)
            Button(
                onClick = onNavigateBack,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(70.dp),
                shape = RoundedCornerShape(20),
                colors = ButtonDefaults.buttonColors(containerColor = HijauTua)
            ) {
                // Trik Outline Text
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "Kembali",
                        fontSize = 20.sp,
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
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Merah
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 5. Button "Log Out" (Putih Outline Merah, Teks Merah)
            OutlinedButton(
                onClick = { showLogoutDialog = true }, // Munculkan Dialog
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(70.dp),
                shape = RoundedCornerShape(20),
                border = BorderStroke(2.dp, Merah), // Outline Merah
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Putih)
            ) {
                Text(
                    text = "Log Out",
                    color = Merah,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}