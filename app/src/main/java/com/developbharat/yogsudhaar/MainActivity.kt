package com.developbharat.yogsudhaar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.developbharat.yogsudhaar.ui.screens.home.HomeScreen
import com.developbharat.yogsudhaar.ui.theme.YogSudhaarTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            YogSudhaarTheme {
                HomeScreen()
            }
        }
    }
}

