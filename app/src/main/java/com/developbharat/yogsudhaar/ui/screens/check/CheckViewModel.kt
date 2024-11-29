package com.developbharat.yogsudhaar.ui.screens.check

import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import androidx.camera.core.ImageProxy
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developbharat.yogsudhaar.common.Screens
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.framework.image.MPImage
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarker
import kotlinx.coroutines.launch

class CheckViewModel constructor(savedStateHandle: SavedStateHandle) : ViewModel() {
    private val _uiState =
        mutableStateOf(CheckScreenState(selectedAsana = Screens.CheckScreen.from(savedStateHandle).asana))
    val uiState: State<CheckScreenState> = _uiState


    fun detectPose(detector: PoseLandmarker, frame: ImageProxy, isFrontCamera: Boolean) {
        viewModelScope.launch {
            val mpImage = convertToModelInput(frame, isFrontCamera)
            val landmarks = detector.detect(mpImage).landmarks()
            var first = landmarks.toList().first()
            var landmark = first.first()
            var x = landmark.x();
            var y = landmark.y();
            var z = landmark.z();
            Log.d("PoseLandmarker", "Landmark: x=$x, y=$y, z=$z")
            frame.close()
        }
    }

    private fun convertToModelInput(frame: ImageProxy, isFrontCamera: Boolean): MPImage {
        // Copy out RGB bits from the frame to a bitmap buffer
        val buffer = Bitmap.createBitmap(frame.width, frame.height, Bitmap.Config.ARGB_8888)
        frame.use { buffer.copyPixelsFromBuffer(frame.planes[0].buffer) }

        val matrix = Matrix().apply {
            // Rotate the frame received from the camera to be in the same direction as it'll be shown
            postRotate(frame.imageInfo.rotationDegrees.toFloat())

            // flip image if user use front camera
            if (isFrontCamera) {
                postScale(-1f, 1f, frame.width.toFloat(), frame.height.toFloat())
            }
        }

        val rotated = Bitmap.createBitmap(buffer, 0, 0, buffer.width, buffer.height, matrix, true)

        // Convert the input Bitmap object to an MPImage object to run inference
        val mpImage = BitmapImageBuilder(rotated).build()
        return mpImage;
    }
}