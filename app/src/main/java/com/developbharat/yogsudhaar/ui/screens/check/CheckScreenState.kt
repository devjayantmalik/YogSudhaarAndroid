package com.developbharat.yogsudhaar.ui.screens.check

import com.developbharat.yogsudhaar.domain.models.Asana

data class CheckScreenState(
    val selectedAsana: Asana,
    val isPoseCorrect: Boolean = true,
    val status: String? = null,
    val totalRepetitionsCount: Int = 0,
    val isDisplaySkeletonEnabled: Boolean = false,
    val isCameraPermissionGranted: Boolean = false
)
