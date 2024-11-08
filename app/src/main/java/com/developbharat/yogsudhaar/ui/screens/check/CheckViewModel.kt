package com.developbharat.yogsudhaar.ui.screens.check

import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developbharat.yogsudhaar.common.Screens
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CheckViewModel constructor(savedStateHandle: SavedStateHandle) : ViewModel() {
    /**
     * Prepare pose detection options
     */
    private val options = AccuratePoseDetectorOptions.Builder()
        .setDetectorMode(AccuratePoseDetectorOptions.STREAM_MODE)
        .setPreferredHardwareConfigs(AccuratePoseDetectorOptions.CPU_GPU)
        .build()
    private val poseDetector = PoseDetection.getClient(options)

    private val _uiState =
        mutableStateOf(CheckScreenState(selectedAsana = Screens.CheckScreen.from(savedStateHandle).asana))
    val uiState: State<CheckScreenState> = _uiState


    @OptIn(ExperimentalGetImage::class)
    fun detectPose(frame: ImageProxy) {
        viewModelScope.launch {
            val image = InputImage.fromMediaImage(frame.image!!, frame.imageInfo.rotationDegrees)
            val detection = findPosture(image)

            // skip frame if detection fails
            if (detection == null) return@launch frame.close();

            // show all landmarks
            // TODO: use detected landmarks to predict correct or incorrect posture.
            val landmarks = detection.allPoseLandmarks
            Log.d("landmarks", landmarks.toString())

            // close the frame finally
            frame.close()
        }
    }

    suspend fun findPosture(frame: InputImage): Pose? {
        return suspendCoroutine { continuation ->
            val task = this.poseDetector.process(frame)
                .addOnSuccessListener { results ->
                    continuation.resume(results)
                }
                .addOnFailureListener { e ->
                    Log.d("error", e.localizedMessage ?: "Unknown error during pose detection.")
                    continuation.resume(null)
                }
        }
    }
}