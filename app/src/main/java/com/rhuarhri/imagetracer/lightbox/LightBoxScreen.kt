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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.rhuarhri.imagetracer.botton_bar.BottomBar
import com.rhuarhri.imagetracer.botton_bar.BottomBarViewModel
import com.rhuarhri.imagetracer.botton_bar.widgets.image_tracing.BlackAndWhiteWidget
import com.rhuarhri.imagetracer.botton_bar.widgets.image_tracing.BlurWidget
import com.rhuarhri.imagetracer.botton_bar.widgets.image_tracing.ColourMergingWidget
import com.rhuarhri.imagetracer.botton_bar.widgets.image_tracing.ColourOverlayWidget
import com.rhuarhri.imagetracer.botton_bar.widgets.image_tracing.ColourRemoverWidget
import com.rhuarhri.imagetracer.botton_bar.widgets.image_tracing.ContrastWidget
import com.rhuarhri.imagetracer.botton_bar.widgets.image_tracing.EdgeDetectionWidget
import com.rhuarhri.imagetracer.botton_bar.widgets.image_tracing.FlipWidget
import com.rhuarhri.imagetracer.botton_bar.widgets.image_tracing.ImageSegmentationWidget
import com.rhuarhri.imagetracer.botton_bar.widgets.image_tracing.InvertWidget
import com.rhuarhri.imagetracer.botton_bar.widgets.image_tracing.MonochromeWidget
import com.rhuarhri.imagetracer.botton_bar.widgets.image_tracing.ResetWidget
import com.rhuarhri.imagetracer.botton_bar.widgets.image_tracing.ResizeWidget
import com.rhuarhri.imagetracer.botton_bar.widgets.image_tracing.TransparencyWidget
import com.rhuarhri.imagetracer.ui.theme.ImageTracerTheme

class LightBoxScreen {
    
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Screen(controller : NavController) {

        val bottomBarViewModel : BottomBarViewModel = viewModel()
        val lightBoxViewModel : LightBoxViewModel = hiltViewModel()

        val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
            var scale = lightBoxViewModel.scale.value
            scale *= zoomChange
            lightBoxViewModel.setScale(scale)

            var rotation = lightBoxViewModel.rotation.value
            rotation += rotationChange
            lightBoxViewModel.setRotation(rotation)

            var offset = lightBoxViewModel.offset.value
            offset += offsetChange
            lightBoxViewModel.setOffset(offset)
        }

        val tracingImage by lightBoxViewModel.tracingBitmap.collectAsState()

        ImageTracerTheme {
            // A surface container using the 'background' color from the theme
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                val darkMode by lightBoxViewModel.darkMode.collectAsState()
                val background = if (darkMode) {
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
                                    IconToggleButton(checked = darkMode, onCheckedChange = {
                                        lightBoxViewModel.setDarkMode(!darkMode)
                                    }) {
                                        if (darkMode) {
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
                                    val scale by lightBoxViewModel.scale.collectAsState()
                                    val rotation by lightBoxViewModel.rotation.collectAsState()
                                    val offset by lightBoxViewModel.offset.collectAsState()

                                    val enablePinchToZoom by lightBoxViewModel.enablePinchToZoom.collectAsState()
                                    val isBarVisible by bottomBarViewModel.isBarVisible.collectAsState()

                                    //the user has allowed pinch to zoom and the editing
                                    //tools are visible so the user is currently editing
                                    val enabled = isBarVisible && enablePinchToZoom

                                    Box(modifier = Modifier
                                        .fillMaxSize()
                                        .padding(8.dp),
                                        contentAlignment = Alignment.Center) {
                                        Image(bitmap = it.asImageBitmap(), contentDescription = "Image being traced",
                                            modifier = Modifier
                                                .fillMaxSize()
                                                /*.pointerInput(Unit) {
                                                    detectTapGestures { offset ->

                                                    }
                                                    detectDragGestures { change, dragAmount ->  }
                                                }*/
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
                        val isBarVisible by bottomBarViewModel.isBarVisible.collectAsState()
                        FloatingActionButton(onClick = {
                            if (isBarVisible) {
                                bottomBarViewModel.hide()
                            } else {
                                bottomBarViewModel.show()
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

                        BottomBar.Bar(viewModel = bottomBarViewModel,
                            widgets = listOf(
                                ResetWidget(
                                    reset = {
                                        lightBoxViewModel.resetAll()
                                    },
                                    close = {
                                        bottomBarViewModel.hideExtension()
                                    }
                                ),
                                ResizeWidget(
                                    state =
                                    lightBoxViewModel.enablePinchToZoom.collectAsState(),
                                    set = {
                                        lightBoxViewModel.setEnablePinchToZoom(it)
                                    },
                                    reset = {
                                        lightBoxViewModel.resetEnablePinchToZoom()
                                        lightBoxViewModel.resetOffset()
                                        lightBoxViewModel.resetScale()
                                        lightBoxViewModel.resetRotation()
                                    },
                                    close = {
                                        bottomBarViewModel.hideExtension()
                                    }
                                ),
                                FlipWidget(
                                    state = lightBoxViewModel.flip.collectAsState(),
                                    set = {
                                        lightBoxViewModel.setFlip(it)
                                        lightBoxViewModel.edit()
                                    },
                                    reset = {
                                        lightBoxViewModel.resetFlip()
                                        lightBoxViewModel.edit()
                                    },
                                    close = {
                                        bottomBarViewModel.hideExtension()
                                    }
                                ),
                                TransparencyWidget(
                                    state = lightBoxViewModel.transparency.collectAsState(),
                                    set = {
                                        lightBoxViewModel.setTransparency(it)
                                    },
                                    edit = {
                                        lightBoxViewModel.edit()
                                    },
                                    reset = {
                                        lightBoxViewModel.resetTransparency()
                                        lightBoxViewModel.edit()
                                    },
                                    close = {
                                        bottomBarViewModel.hideExtension()
                                    }
                                ),
                                EdgeDetectionWidget(
                                    state = lightBoxViewModel.edgeDetection.collectAsState(),
                                    set = {
                                        lightBoxViewModel.setEdgeDetection(it)
                                        lightBoxViewModel.edit()
                                    },
                                    reset = {
                                        lightBoxViewModel.resetEdgeDetection()
                                        lightBoxViewModel.edit()
                                    },
                                    close = {
                                        bottomBarViewModel.hideExtension()
                                    }
                                ),
                                ImageSegmentationWidget(
                                    state = lightBoxViewModel.imageSegmentation.collectAsState(),
                                    set = {
                                        lightBoxViewModel.setImageSegmentation(it)
                                        lightBoxViewModel.edit()
                                    },
                                    reset = {
                                        lightBoxViewModel.resetImageSegmentation()
                                        lightBoxViewModel.edit()
                                    },
                                    close = {
                                        bottomBarViewModel.hideExtension()
                                    }
                                ),
                                ColourRemoverWidget(
                                    redState = lightBoxViewModel.red.collectAsState(),
                                    greenState = lightBoxViewModel.green.collectAsState(),
                                    blueState = lightBoxViewModel.blue.collectAsState(),
                                    set = { red, green, blue ->
                                        lightBoxViewModel.setColourRemover(red, green, blue)
                                    },
                                    edit = { _, _, _ ->
                                        lightBoxViewModel.edit()
                                    },
                                    reset = {
                                        lightBoxViewModel.resetColourRemover()
                                        lightBoxViewModel.edit()
                                    },
                                    close = {
                                        bottomBarViewModel.hideExtension()
                                    }
                                ),
                                ColourMergingWidget(
                                    state = lightBoxViewModel.colourMerging.collectAsState(),
                                    set = {
                                        lightBoxViewModel.setColourMerging(it)
                                        lightBoxViewModel.edit()
                                    },
                                    reset = {
                                        lightBoxViewModel.resetColourMerging()
                                        lightBoxViewModel.edit()
                                    },
                                    close = {
                                        bottomBarViewModel.hideExtension()
                                    }
                                ),
                                BlurWidget(
                                    state = lightBoxViewModel.blur.collectAsState(),
                                    set = {
                                        lightBoxViewModel.setBlur(it)
                                        lightBoxViewModel.edit()
                                    },
                                    reset = {
                                        lightBoxViewModel.resetBlur()
                                        lightBoxViewModel.edit()
                                    },
                                    close = {
                                        bottomBarViewModel.hideExtension()
                                    }
                                ),
                                ContrastWidget(
                                    contrastState = lightBoxViewModel.contrast.collectAsState(),
                                    brightnessState = lightBoxViewModel.brightness.collectAsState(),
                                    set = {contrast, brightness ->
                                        lightBoxViewModel.setContrast(contrast, brightness)
                                    },
                                    edit = { _, _ ->
                                        lightBoxViewModel.edit()
                                    },
                                    reset = {
                                        lightBoxViewModel.resetContrast()
                                        lightBoxViewModel.edit()
                                    },
                                    close = {
                                        bottomBarViewModel.hideExtension()
                                    }
                                ),
                                ColourOverlayWidget(
                                    state = lightBoxViewModel.colourOverlay.collectAsState(),
                                    set = {
                                        lightBoxViewModel.setColourOverlay(it)
                                        lightBoxViewModel.edit()
                                    },
                                    reset = {
                                        lightBoxViewModel.resetColourOverlay()
                                        lightBoxViewModel.edit()
                                    },
                                    close = {
                                        bottomBarViewModel.hideExtension()
                                    }
                                ),
                                InvertWidget(
                                    state = lightBoxViewModel.invert.collectAsState(),
                                    set = {
                                        lightBoxViewModel.setInvert(it)
                                        lightBoxViewModel.edit()
                                    },
                                    reset = {
                                        lightBoxViewModel.resetInvert()
                                        lightBoxViewModel.edit()
                                    },
                                    close = {
                                        bottomBarViewModel.hideExtension()
                                    }
                                ),
                                MonochromeWidget(
                                    state = lightBoxViewModel.monochrome.collectAsState(),
                                    set = {
                                        lightBoxViewModel.setMonochrome(it)
                                        lightBoxViewModel.edit()
                                    },
                                    reset = {
                                        lightBoxViewModel.resetMonochrome()
                                        lightBoxViewModel.edit()
                                    },
                                    close = {
                                        bottomBarViewModel.hideExtension()
                                    }
                                ),
                                BlackAndWhiteWidget(
                                    state = lightBoxViewModel.blackAndWhite.collectAsState(),
                                    set = {
                                        lightBoxViewModel.setBlackAndWhite(it)
                                        lightBoxViewModel.edit()
                                    },
                                    reset = {
                                        lightBoxViewModel.resetBlackAndWhite()
                                        lightBoxViewModel.edit()
                                    },
                                    close = {
                                        bottomBarViewModel.hideExtension()
                                    }
                                )
                            ))
                    }
                )
            }
        }
    }
}