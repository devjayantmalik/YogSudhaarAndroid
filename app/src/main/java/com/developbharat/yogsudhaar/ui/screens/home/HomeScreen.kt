package com.developbharat.yogsudhaar.ui.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.developbharat.yogsudhaar.R
import com.developbharat.yogsudhaar.common.Screens
import com.developbharat.yogsudhaar.domain.models.Asana
import com.developbharat.yogsudhaar.domain.models.CameraMode
import com.developbharat.yogsudhaar.ui.screens.components.SmallTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    // TODO: replace list with actual asana list from constants
    val asanas: List<Asana> = listOf(
        Asana(
            name = "Bhujangasana",
            resourceId = R.drawable.icon_bhujangasana,
            cameraMode = CameraMode.Landscape
        ),
        Asana(
            name = "Veerbhadrasana",
            resourceId = R.drawable.icon_veerbhadrasana,
            cameraMode = CameraMode.Portrait
        ),
        Asana(
            name = "Vrikshana",
            resourceId = R.drawable.icon_vrikshana,
            cameraMode = CameraMode.Portrait
        )
    )


    Scaffold(topBar = {
        SmallTopBar(
            title = "Yog Sudhaar",
            subtitle = "Encourage correct Yoga Exercises."
        )
    }) { paddingValues ->
        Surface(modifier = Modifier.padding(paddingValues)) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(asanas) { item ->
                    Card(onClick = {
                        navController.navigate(Screens.CheckScreen(asana = item))
                    }, border = BorderStroke(1.dp, Color.Gray), shape = RoundedCornerShape(5.dp)) {
                        Image(
                            painter = painterResource(item.resourceId),
                            contentDescription = "${item.name} asana",
                            modifier = Modifier
                                .height(100.dp)
                                .fillMaxWidth()
                                .clip(CircleShape)
                                .padding(10.dp)
                                .align(Alignment.CenterHorizontally),
                        )
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(item.name, style = MaterialTheme.typography.titleMedium)
                            Text(
                                "${item.cameraMode.name} Mode",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }
    }
}