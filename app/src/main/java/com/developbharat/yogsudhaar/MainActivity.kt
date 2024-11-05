package com.developbharat.yogsudhaar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.developbharat.yogsudhaar.common.Screens
import com.developbharat.yogsudhaar.ui.screens.check.CheckScreen
import com.developbharat.yogsudhaar.ui.screens.home.HomeScreen
import com.developbharat.yogsudhaar.ui.theme.YogSudhaarTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            YogSudhaarTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Screens.HomeScreen) {
                    // chat screens
                    composable<Screens.HomeScreen> { HomeScreen(navController) }
                    composable<Screens.CheckScreen>(typeMap = Screens.CheckScreen.typeMap) { CheckScreen(navController) }
                }
            }
        }
    }
}

