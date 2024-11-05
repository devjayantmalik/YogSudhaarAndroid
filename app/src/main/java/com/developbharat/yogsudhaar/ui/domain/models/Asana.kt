package com.developbharat.yogsudhaar.ui.domain.models

enum class CameraMode {
    Portrait,
    Landscape
}

data class Asana(
    val name: String,
    val resourceId: Int,
    val cameraMode: CameraMode
)