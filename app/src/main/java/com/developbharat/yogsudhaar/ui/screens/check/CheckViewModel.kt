package com.developbharat.yogsudhaar.ui.screens.check

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.developbharat.yogsudhaar.common.Screens

class CheckViewModel constructor(savedStateHandle: SavedStateHandle) : ViewModel() {
    private val _uiState =
        mutableStateOf(CheckScreenState(selectedAsana = Screens.CheckScreen.from(savedStateHandle).asana))
    val uiState: State<CheckScreenState> = _uiState
}