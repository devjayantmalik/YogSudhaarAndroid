package com.developbharat.yogsudhaar.domain.api.dto

import com.google.gson.annotations.SerializedName

data class IsPoseCorrectResult(
    @SerializedName("is_pose_correct") val isPoseCorrect: Boolean,
    @SerializedName("message") val message: String
)