package com.developbharat.yogsudhaar.ui.domain.common

import kotlinx.serialization.Serializable

@Serializable
sealed interface Screens {
    @Serializable
    data object HomeScreen : Screens

    @Serializable
    data object CheckScreen : Screens
}