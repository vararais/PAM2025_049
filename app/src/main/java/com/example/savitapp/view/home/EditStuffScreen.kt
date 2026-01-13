package com.example.savitapp.view.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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

    LaunchedEffect(Unit) {
        viewModel.loadStuffData(userId, stuffId)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Edit Barang") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color(0xFFA3B8A3))
            )
        },
        containerColor = Color(0xFFF5EFE6)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Memanggil FormInput yang ada di file EntryStuffScreen.kt
            FormInput(
                insertUiEvent = viewModel.uiState.insertUiEvent,
                onValueChange = viewModel::updateUiState,
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    coroutineScope.launch {
                        viewModel.updateStuff(userId, stuffId) {
                            navigateBack()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7D9581))
            ) {
                Text("Update Data")
            }

            OutlinedButton(
                onClick = navigateBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Batal", color = Color.Black)
            }
        }
    }
}