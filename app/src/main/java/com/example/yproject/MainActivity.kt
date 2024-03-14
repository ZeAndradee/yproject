package com.example.yproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.yproject.ui.theme.YProjectTheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

val imbiribeiraState = LatLng(-7.995005654053916, -35.040734136716246)
val defaultCameraPosition = CameraPosition.fromLatLngZoom(
        imbiribeiraState,
        15f,
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YProjectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val cameraPositionState = rememberCameraPositionState{
                        position = defaultCameraPosition
                    }
                    Column (modifier = Modifier.fillMaxSize() .border(1.dp, Color.Red)){ // Column
                        Box (modifier = Modifier .fillMaxHeight(0.95f)){
                            GoogleMapView(
                                modifier = Modifier .fillMaxSize(),
                                cameraPositionState = cameraPositionState,
                            )
                        }
                        Row (modifier = Modifier
                            .fillMaxHeight(0.2f)
                            .fillMaxWidth()
                            .background(Color.Black)){
                        }
                    }
                    }
                }
            }
        }
    }


@Composable
fun GoogleMapView(
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState,
    onMapLoaded: () -> Unit = {},
){

    var mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(compassEnabled = false)
        )
    }
    var mapProperties by remember {
        mutableStateOf(
            MapProperties(mapType = MapType.TERRAIN)
        )
    }

    GoogleMap(
        modifier = modifier,
        onMapLoaded = onMapLoaded,
        uiSettings = mapUiSettings,
        properties = mapProperties,
        cameraPositionState = cameraPositionState,
    )
}