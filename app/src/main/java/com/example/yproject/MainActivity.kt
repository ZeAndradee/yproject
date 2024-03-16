package com.example.yproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.yproject.ui.theme.YProjectTheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState

data class BottomNavMenu(
    val Title: String,
    val SelectedIcon: ImageVector,
    val UnselectedIcon: ImageVector,
    var NotificationBadge: MutableState<Int?>? = null,
    val Route: String,
)

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


                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val cameraPositionState = rememberCameraPositionState{
                        position = defaultCameraPosition
                    }
                    val items = listOf(
                        BottomNavMenu(
                            Title = "Home",
                            SelectedIcon = Icons.Filled.Home,
                            UnselectedIcon = Icons.Outlined.Home,
                            Route = "Home"
                        ),
                        BottomNavMenu(
                            Title = "Comunidade",
                            SelectedIcon = Icons.Filled.Notifications,
                            UnselectedIcon = Icons.Outlined.Notifications,
                            Route = "Comunidade",
                            NotificationBadge = remember { mutableStateOf(13) }

                        ),
                        BottomNavMenu(
                            Title = "Salvos",
                            SelectedIcon = Icons.Default.Favorite,
                            UnselectedIcon = Icons.Default.FavoriteBorder,
                            Route = "Saved"
                        ),
                        BottomNavMenu(
                            Title = "Perfil",
                            SelectedIcon = Icons.Default.Person,
                            UnselectedIcon = Icons.Outlined.Person,
                            Route = "profile"
                        ),
                    )
                    var selectedIndexItem by rememberSaveable {
                        mutableIntStateOf(0)

                    }

                        Scaffold (
                            bottomBar = {
                                NavigationBar {
                                    items.forEachIndexed { index, item ->
                                        NavigationBarItem(
                                            selected = selectedIndexItem == index,
                                            onClick = {
                                                selectedIndexItem = index
                                                if (index == 1){
                                                    item.NotificationBadge?.value = null
                                                }
                                            },
                                            label = {
                                                Text(text = item.Title)
                                            },
                                            icon = {
                                                BadgedBox(
                                                    badge = {
                                                            if (item.NotificationBadge?.value != null){
                                                                Badge{
                                                                    Text(text = item.NotificationBadge?.value.toString())
                                                                }
                                                            }
                                                    }
                                                ) {
                                                    Icon(
                                                        imageVector = if (index == selectedIndexItem) item.SelectedIcon else item.UnselectedIcon,
                                                        contentDescription = item.Title,
                                                        )
                                                }
                                            })
                                    }
                                }
                            },
                            content = { paddingValues ->
                                GoogleMapView(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(paddingValues),
                                    cameraPositionState = cameraPositionState
                                )
                            }
                        )
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
            MapUiSettings(compassEnabled = false, zoomControlsEnabled = false)
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