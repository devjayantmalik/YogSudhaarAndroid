package com.developbharat.yogsudhaar.domain.api.dto

import com.google.gson.annotations.SerializedName

data class PoseData(
    @SerializedName("poses") val poses: List<List<Double>>
)

data class IsPoseCorrectInput(
    @SerializedName("frames") val frames: List<PoseData>,
)