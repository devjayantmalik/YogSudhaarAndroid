package com.developbharat.yogsudhaar.ui.screens.check

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class CheckViewModel : ViewModel() {
    private val _uiState = mutableStateOf(CheckScreenState())
    val uiState: State<CheckScreenState> = _uiState
}