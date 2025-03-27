package com.developbharat.yogsudhaar.ui.screens.check

import android.content.pm.ActivityInfo
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SwitchCamera
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.developbharat.yogsudhaar.common.CameraOptions
import com.developbharat.yogsudhaar.common.SetScreenOrientation
import com.developbharat.yogsudhaar.domain.models.CameraMode
import com.developbharat.yogsudhaar.ui.screens.components.CameraPreview
import com.developbharat.yogsudhaar.ui.screens.components.SmallTopBar
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarker
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarker.PoseLandmarkerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckScreen(navController: NavController, viewModel: CheckViewModel = viewModel()) {
    val uiState = viewModel.uiState.value
    val context = LocalContext.current;
    var isFrontCameraSelected by remember { mutableStateOf(false) }

    // set screen orientation
    if (uiState.selectedAsana.cameraMode == CameraMode.Portrait) {
        SetScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    } else if (uiState.selectedAsana.cameraMode == CameraMode.Landscape) {
        SetScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
    }

    val detector = remember {
        val modelOptions = PoseLandmarkerOptions.builder()
            .setBaseOptions(CameraOptions.baseModelOptions)
            .setRunningMode(RunningMode.IMAGE)
            .build()

        PoseLandmarker.createFromOptions(context, modelOptions)
    }


    // TODO: ask for camera permission if not granted, if declined show error instead of camera ui
    LaunchedEffect(Unit) {
        CameraOptions.frameAnalysisOptions.setAnalyzer(Dispatchers.Default.asExecutor()) { frame ->
            viewModel.detectPose(detector, frame, isFrontCameraSelected, context)
        }
    }

    Scaffold(topBar = {
        SmallTopBar(
            title = uiState.selectedAsana.name,
            subtitle = uiState.status ?: "${uiState.selectedAsana.cameraMode.name} Mode",
            actions = {
                IconButton(onClick = { isFrontCameraSelected = !isFrontCameraSelected }) {
                    Icon(
                        Icons.Outlined.SwitchCamera,
                        contentDescription = "Switch Camera"
                    )
                }
            }
        )
    }) { paddingValues ->
        Surface(modifier = Modifier.padding(paddingValues)) {
            CameraPreview(
                isFrontCameraSelected = isFrontCameraSelected,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}