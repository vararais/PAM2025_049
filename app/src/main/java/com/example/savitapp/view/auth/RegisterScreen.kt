package com.example.savitapp.view.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.savitapp.viewmodel.AuthUiState
import com.example.savitapp.viewmodel.PenyediaViewModel
import com.example.savitapp.viewmodel.RegisterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onNavigateBack: () -> Unit,
    viewModel: RegisterViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    var nama by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    val uiState = viewModel.uiState

    val DarkGreen = Color(0xFF1B3B28)
    val LightGreen = Color(0xFF7D9581)
    val Cream = Color(0xFFF5EFE6)

    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Success) {
            Toast.makeText(context, uiState.message, Toast.LENGTH_SHORT).show()
            onNavigateBack()
        } else if (uiState is AuthUiState.Error) {
            Toast.makeText(context, uiState.message, Toast.LENGTH_SHORT).show()
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(DarkGreen), contentAlignment = Alignment.Center) {
        Card(
            modifier = Modifier.padding(24.dp).fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = LightGreen),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Register Here", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)

                OutlinedTextField(
                    value = nama, onValueChange = { nama = it },
                    label = { Text("Nama") },
                    shape = RoundedCornerShape(30.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Cream, unfocusedContainerColor = Cream,
                        focusedBorderColor = Color.Transparent, unfocusedBorderColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = email, onValueChange = { email = it },
                    label = { Text("Email") },
                    shape = RoundedCornerShape(30.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Cream, unfocusedContainerColor = Cream,
                        focusedBorderColor = Color.Transparent, unfocusedBorderColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                OutlinedTextField(
                    value = password, onValueChange = { password = it },
                    label = { Text("Password") },
                    shape = RoundedCornerShape(30.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Cream, unfocusedContainerColor = Cream,
                        focusedBorderColor = Color.Transparent, unfocusedBorderColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )

                Button(
                    onClick = { viewModel.register(nama, email, password) },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA3B8A3)),
                    shape = RoundedCornerShape(30.dp)
                ) {
                    if (uiState is AuthUiState.Loading) {
                        CircularProgressIndicator(color = Color.Black, modifier = Modifier.size(24.dp))
                    } else {
                        Text("Register", color = Color.Black, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Text(
                    text = "Already have account? Login Here!",
                    color = Color.Black, fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onNavigateBack() },
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }
}