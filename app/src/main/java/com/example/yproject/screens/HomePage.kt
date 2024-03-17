package com.example.yproject.screens
import android.app.Activity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.yproject.utils.googleMaps.checkAndRequestLocationPermission
import com.example.yproject.utils.googleMaps.getLastLocation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
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


@Composable
fun HomePage(){

    val context = LocalContext.current
    val activity = context as Activity
    var currentPosition by remember { mutableStateOf<LatLng?>(null) }
    var isLocationObtained by remember { mutableStateOf(false) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(-8.11799711959781, -34.91363173260206), // Use a posição padrão se a posição atual ainda não estiver disponível
            15f
        )
    }


    activity.getLastLocation { position ->
        currentPosition = position
        isLocationObtained = true
    }

    LaunchedEffect(currentPosition) {
        if (currentPosition != null) {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(
                currentPosition!!,
                15f
            )
        }
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
        floatingActionButton = {
            Column (modifier = Modifier ){
                FloatingActionButton(onClick = {
                        cameraPositionState.position = CameraPosition.fromLatLngZoom(
                        currentPosition ?: LatLng(-8.11799711959781, -34.91363173260206), //OnClick a posição atual do user é definida
                        15f
                    )
                }, shape = RoundedCornerShape(50)) {
                    Icon(imageVector = Icons.Default.LocationOn, contentDescription = "Adicionar Alertas")
                }
                Spacer(modifier = Modifier.height(16.dp))
                FloatingActionButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Adicionar Alertas")
                }
            }
        },
        content = { paddingValues ->
            GoogleMapView(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                cameraPositionState = cameraPositionState
            ){checkAndRequestLocationPermission(context)
                }
        }
    )
}
@Composable
fun GoogleMapView(
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState,
    onMapLoaded: () -> Unit = {},
){

    val mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(compassEnabled = false, zoomControlsEnabled = false)
        )
    }
    val mapProperties by remember {
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
