package com.developbharat.yogsudhaar.ui.screens.check

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.camera.core.ImageProxy
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.compose.runtime.remember
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developbharat.yogsudhaar.common.Screens
import com.developbharat.yogsudhaar.domain.api.IRemoteSource
import com.developbharat.yogsudhaar.domain.api.dto.IsPoseCorrectInput
import com.developbharat.yogsudhaar.domain.api.dto.PoseData
import com.developbharat.yogsudhaar.domain.splitters.VrksasanaSplitter
import com.developbharat.yogsudhaar.ui.screens.check.components.OverlayView
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.framework.image.MPImage
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarker
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Locale
import kotlin.jvm.optionals.getOrNull


class CheckViewModel constructor(savedStateHandle: SavedStateHandle) : ViewModel() {
    private var framesData: MutableList<List<Double>> = mutableListOf()
    private val _uiState =
        mutableStateOf(CheckScreenState(selectedAsana = Screens.CheckScreen.from(savedStateHandle).asana))
    val uiState: State<CheckScreenState> = _uiState
    val retrofit = Retrofit.Builder()
        .baseUrl("https://cdis.iitk.ac.in/yog-sudhar/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val remoteSource = retrofit.create(IRemoteSource::class.java)
    val overlayViewRef = mutableStateOf<OverlayView?>(null)
    var tts: TextToSpeech? = null
    var logs: String = ""
    var frameNo: Int = -1


    fun checkAndUpdateCameraPermission(context: Context) {
        val isGranted = isCameraPermissionGranted(context)
        _uiState.value = _uiState.value.copy(isCameraPermissionGranted = isGranted)
    }

    private fun isCameraPermissionGranted(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context, android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }


    fun initTts(context: Context) {
        tts = TextToSpeech(context, { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = tts?.setLanguage(Locale.US)

                if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                }
            }
        });
    }

     fun speak(text: String) {
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "tts1")
    }

    fun setDisplaySkeleton(enabled: Boolean = false) {
        _uiState.value = _uiState.value.copy(isDisplaySkeletonEnabled = enabled)
    }

    fun detectPose(
        detector: PoseLandmarker,
        frame: ImageProxy,
        isFrontCamera: Boolean,
        context: Context
    ) {
        frameNo += 1
//        Log.d("frames", logs)
        viewModelScope.launch {
//            // TODO: Remove 2 lines to stop reading static image, and rather switch to live camera stream
//            val bitmap = ResourcesCompat.getDrawable(context.resources, R.drawable.sample, null)!!
//                .toBitmap(config = Bitmap.Config.ARGB_8888)
//            val mpImage: MPImage = BitmapImageBuilder(bitmap).build()
            val mpImage = convertToModelInput(frame, isFrontCamera)
            val detectedResult = detector.detect(mpImage)
            val landmarksList = detectedResult.landmarks()
            if (landmarksList.isEmpty()) {
                frame.close()
                return@launch
            }
            val landmarks = landmarksList.first()

            // draw landmarks
            if (_uiState.value.isDisplaySkeletonEnabled) {
                overlayViewRef.value?.setResults(
                    detectedResult,
                    mpImage.height,
                    mpImage.width,
                    RunningMode.IMAGE
                )
            }

            // Format poses and add to frames.
            val poses = mutableListOf<Double>()
            poses.add(frameNo.toDouble())
            landmarks.forEach { item ->
                poses.add(item.x().toDouble())
                poses.add(item.y().toDouble())
                poses.add(item.z().toDouble())
                poses.add(item.visibility().getOrNull()?.toDouble() ?: 0.0)
            }
            framesData.add(poses)
//            val framesJoined = framesData.joinToString("\n") { it.joinToString(",") }
//            logs += framesJoined + "\n"
            Log.d("frames", poses.joinToString(","))
            val indexes = VrksasanaSplitter().split(framesData)
//            Log.d("frames", "after split, indexes: $indexes")
            Log.d("frames", "after split, indexes: $indexes")
//            logs += "after split, indexes: $indexes\n"

            if (indexes == null) {
                frame.close();
                return@launch
            }
            if (indexes.second - indexes.first <= 7) {
                speak("Please do the exercise Slowly or stand closer to camera.")
                Toast.makeText(
                    context,
                    "Please do the exercise Slowly or stand closer to camera.",
                    Toast.LENGTH_LONG
                ).show()
                framesData.clear()
                frame.close()
                return@launch
            }

            // Get repetition data and remove repetition from frames data
            val repetition = framesData.subList(indexes.first, indexes.second + 1).toList()

//            framesData = framesData.subList(indexes.second, framesData.count())
            framesData.clear()

            // Check if pose is correct.
            try {
                val data = IsPoseCorrectInput(frames = listOf(PoseData(poses = repetition)))
                val response = remoteSource.isPoseCorrect(data)
                _uiState.value = _uiState.value.copy(
                    isPoseCorrect = response.isPoseCorrect,
                    status = response.message + "(${repetition.first().first()},${
                        repetition.last().first()
                    })",
                    totalRepetitionsCount = _uiState.value.totalRepetitionsCount + 1
                )
            } catch (exception: Exception) {
                _uiState.value = _uiState.value.copy(
                    isPoseCorrect = false,
                    status = exception.localizedMessage ?: ""
                )
            }


//            println(repetition)

//            val rows =
//                first.map { "${it.x()}, ${it.y()}, ${it.z()}, ${it.visibility()}, ${it.presence()}" }
//            Log.d("PoseLandmarker", rows.joinToString("\n"))
//            Log.d("PoseLandmarker", "Landmark: x=${landmarks}")
//            var first = landmarks.toList().first()
//            var landmark = first.first()
//            var x = landmark.x();
//            var y = landmark.y();
//            var z = landmark.z();
//            Log.d("PoseLandmarker", "Landmark: x=$x, y=$y, z=$z")

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