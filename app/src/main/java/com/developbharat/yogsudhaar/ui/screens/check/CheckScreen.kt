package com.developbharat.yogsudhaar.ui.screens.check

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckScreen(navController: NavController, viewModel: CheckViewModel = viewModel()) {
    val uiState = viewModel.uiState.value

    Scaffold(topBar = { TopAppBar(title = { Text("Yog Sudhaar") }) }) { paddingValues ->
        Surface(modifier = Modifier.padding(paddingValues)) {
            Text("Welcome to Check Screen: Is OK: ${uiState.isOk}")
        }
    }
}