package com.rhuarhri.imagetracer.lightbox

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.rhuarhri.imagetracer.botton_bar.ImageTracingBottomBar
import com.rhuarhri.imagetracer.botton_bar.ImageTracingBottomBarViewModel
import com.rhuarhri.imagetracer.ui.theme.ImageTracerTheme

class LightBoxScreen {
    
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Screen(controller : NavController) {

        val barViewModel : ImageTracingBottomBarViewModel = hiltViewModel()

        var darkModeOn by remember {
            mutableStateOf(false)
        }

        val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
            var scale = barViewModel.scale.value
            scale *= zoomChange
            barViewModel.setScale(scale)

            var rotation = barViewModel.rotation.value
            rotation += rotationChange
            barViewModel.setRotation(rotation)

            var offset = barViewModel.offset.value
            offset += offsetChange
            barViewModel.setOffset(offset)
        }

        val tracingImage by barViewModel.tracingBitmap.collectAsState()

        ImageTracerTheme {
            // A surface container using the 'background' color from the theme
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                val background = if (darkModeOn) {
                    Color.Black
                } else {
                    Color.White
                }

                Scaffold(
                    content = {
                        Box(modifier = Modifier
                            .fillMaxSize()
                            .padding(it)
                            .background(background)
                        ) {
                            Column() {

                                Row(horizontalArrangement = Arrangement.Start) {
                                    /*Tis is used to change the background colour of the screen.
                                    The idea being if te user is tracing on the screen. Then they
                                     can change the background between black and white. Depending
                                      on which they find easier to draw on.
                                     */
                                    IconToggleButton(checked = darkModeOn, onCheckedChange = {
                                        darkModeOn = !darkModeOn
                                    }) {
                                        if (darkModeOn) {
                                            Icon(
                                                imageVector = Icons.Default.DarkMode,
                                                contentDescription = "Dark Mode",
                                                tint = Color.White
                                            )
                                        } else {
                                            Icon(
                                                imageVector = Icons.Default.LightMode,
                                                contentDescription = "Light Mode",
                                                tint = Color.Black
                                            )
                                        }
                                    }
                                }

                                tracingImage?.let {
                                    val scale by barViewModel.scale.collectAsState()
                                    val rotation by barViewModel.rotation.collectAsState()
                                    val offset by barViewModel.offset.collectAsState()

                                    val enablePinchToZoom by barViewModel.enablePinchToZoom.collectAsState()
                                    val isBarVisible by barViewModel.isBarVisible.collectAsState()

                                    //the user has allowed pinch to zoom and the editing
                                    //tools are visible so the user is currently editing
                                    val enabled = isBarVisible && enablePinchToZoom

                                    Box(modifier = Modifier.fillMaxSize().padding(8.dp),
                                        contentAlignment = Alignment.Center) {
                                        Image(bitmap = it.asImageBitmap(), contentDescription = "Image being traced",
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .graphicsLayer(
                                                    scaleX = scale,
                                                    scaleY = scale,
                                                    rotationZ = rotation,
                                                    translationX = offset.x,
                                                    translationY = offset.y
                                                )
                                                .transformable(
                                                    state = state,
                                                    enabled = enabled
                                                )
                                            )
                                    }
                                }
                            }
                        }
                    },
                    floatingActionButton = {
                        val isBarVisible by barViewModel.isBarVisible.collectAsState()
                        FloatingActionButton(onClick = {
                            if (isBarVisible) {
                                barViewModel.hide()
                            } else {
                                barViewModel.show()
                            }
                        }) {
                            if (!isBarVisible) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit Menu Button",
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Edit Menu Button",
                                )
                            }
                        }
                    },
                    floatingActionButtonPosition = FabPosition.Center,
                    bottomBar = {

                        ImageTracingBottomBar().Bar(viewModel = barViewModel)

                    }
                )
            }
        }
    }
}