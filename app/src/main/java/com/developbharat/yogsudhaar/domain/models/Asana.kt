package com.developbharat.yogsudhaar.domain.models

import kotlinx.serialization.Serializable

@Serializable
enum class CameraMode {
    Portrait,
    Landscape
}

@Serializable
data class Asana(
    val name: String,
    val resourceId: Int,
    val cameraMode: CameraMode
)