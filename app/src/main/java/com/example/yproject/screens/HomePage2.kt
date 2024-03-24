package com.example.yproject.screens
import android.app.Activity
import android.util.Log
import android.widget.Toast
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

data class BottomNavMenu2(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    var notificationBadge: MutableState<Int?>? = null,
    val route: String,
)

@Composable
fun HomePage2(){

    val context = LocalContext.current
    val activity = context as Activity
    var currentPosition by remember { mutableStateOf<LatLng?>(null) }
    var isLocationObtained by remember { mutableStateOf(false) }
    var markers by remember { mutableStateOf(listOf<LatLng>()) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(-8.11799711959781, -34.91363173260206),
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
        } else {
            activity.getLastLocation { position ->
                currentPosition = position
                isLocationObtained = true
                if (currentPosition != null) {
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(
                        currentPosition!!,
                        15f
                    )
                }
            }
        }
    }


    val items = listOf(
        BottomNavMenu2(
            title = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            route = "Home"
        ),
        BottomNavMenu2(
            title = "Comunidade",
            selectedIcon = Icons.Filled.Notifications,
            unselectedIcon = Icons.Outlined.Notifications,
            route = "Comunidade",
            notificationBadge = remember { mutableStateOf(13) }

        ),
        BottomNavMenu2(
            title = "Salvos",
            selectedIcon = Icons.Default.Favorite,
            unselectedIcon = Icons.Default.FavoriteBorder,
            route = "Saved"
        ),
        BottomNavMenu2(
            title = "Perfil",
            selectedIcon = Icons.Default.Person,
            unselectedIcon = Icons.Outlined.Person,
            route = "profile"
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
                                item.notificationBadge?.value = null
                            }
                        },
                        label = {
                            Text(text = item.title)
                        },
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (item.notificationBadge?.value != null){
                                        Badge{
                                            Text(text = item.notificationBadge?.value.toString())
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (index == selectedIndexItem) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.title,
                                )
                            }
                        })
                }
            }
        },
        //FloatingActionButton center map
        floatingActionButton = {
            Column (modifier = Modifier ) {
                FloatingActionButton(onClick = {
                    activity.getLastLocation { position ->
                        currentPosition = position
                        isLocationObtained = true
                    }
                    currentPosition?.let {
                        cameraPositionState.position = CameraPosition.fromLatLngZoom(
                            currentPosition!!,
                            15f
                        )
                        Log.d("Current Location: ", "$currentPosition")
                    }
                }, shape = RoundedCornerShape(50)) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Adicionar Alertas"
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                //FloatingActionButton add alerts
                FloatingActionButton(onClick = {
                    var repeatPosition = false
                    markers.forEach { position ->
                        if (currentPosition == position){
                            repeatPosition = true
                        }
                    }
                    if (!repeatPosition){
                        currentPosition?.let {
                            markers = markers + it
                        }
                    }else{
                        Toast.makeText(context, "Não é possivel adicionar um alerta nessa posição.", Toast.LENGTH_SHORT).show()
                    }
                }, shape = RoundedCornerShape(50)) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Adicionar Alertas"
                    )
                }
            }

        },
        content = { paddingValues ->
            GoogleMapView(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                markers = markers,
                cameraPositionState = cameraPositionState,

                ){checkAndRequestLocationPermission(context)
            }
        }
    )
}
@Composable
fun GoogleMapView(
    modifier: Modifier = Modifier,
    markers: List<LatLng>,
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
    ){

        markers.forEach { position ->
            Marker(
                state = rememberMarkerState(position = position),
                title = "Sua localização",
                snippet = "Você está",
                draggable = true,
                icon = BitmapDescriptorFactory.fromResource(com.google.maps.android.compose.utils.R.drawable.common_full_open_on_phone)
            )
        }
    }
}