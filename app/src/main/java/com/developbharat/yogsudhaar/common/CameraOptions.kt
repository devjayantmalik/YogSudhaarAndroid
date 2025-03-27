package com.developbharat.yogsudhaar.common

import androidx.camera.core.ImageAnalysis
import com.google.mediapipe.tasks.core.BaseOptions

object CameraOptions {
    val frameAnalysisOptions = ImageAnalysis.Builder()
        .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
        .build()


    val baseModelOptions = BaseOptions.builder()
        .setModelAssetPath("pose_landmarker_heavy.task") // Place your .task model in assets
        .build()
}