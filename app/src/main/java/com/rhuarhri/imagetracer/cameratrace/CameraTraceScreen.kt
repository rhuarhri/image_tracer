package com.rhuarhri.imagetracer.cameratrace

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
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

class CameraTraceScreen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Screen(navController : NavController) {

        val bottomBarViewModel : BottomBarViewModel = viewModel()
        val cameraTraceViewModel : CameraTraceViewModel = hiltViewModel()

        ImageTracerTheme {
            // A surface container using the 'background' color from the theme
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Scaffold(
                    content = {
                        Box(modifier = Modifier.padding(it)) {
                            val isBarVisible by bottomBarViewModel.isBarVisible.collectAsState()
                            CameraView(isBarVisible, cameraTraceViewModel)
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
                                        cameraTraceViewModel.resetAll()
                                    },
                                    close = {
                                        bottomBarViewModel.hideExtension()
                                    }
                                ),
                                ResizeWidget(
                                    state =
                                    cameraTraceViewModel.enablePinchToZoom.collectAsState(),
                                    set = {
                                        cameraTraceViewModel.setEnablePinchToZoom(it)
                                    },
                                    reset = {
                                        cameraTraceViewModel.resetEnablePinchToZoom()
                                        cameraTraceViewModel.resetOffset()
                                        cameraTraceViewModel.resetScale()
                                        cameraTraceViewModel.resetRotation()
                                    },
                                    close = {
                                        bottomBarViewModel.hideExtension()
                                    }
                                ),
                                FlipWidget(
                                    state = cameraTraceViewModel.flip.collectAsState(),
                                    set = {
                                        cameraTraceViewModel.setFlip(it)
                                        cameraTraceViewModel.edit()
                                    },
                                    reset = {
                                        cameraTraceViewModel.resetFlip()
                                        cameraTraceViewModel.edit()
                                    },
                                    close = {
                                        bottomBarViewModel.hideExtension()
                                    }
                                ),
                                TransparencyWidget(
                                    state = cameraTraceViewModel.transparency.collectAsState(),
                                    set = {
                                        cameraTraceViewModel.setTransparency(it)
                                    },
                                    edit = {
                                        cameraTraceViewModel.edit()
                                    },
                                    reset = {
                                        cameraTraceViewModel.resetTransparency()
                                        cameraTraceViewModel.edit()
                                    },
                                    close = {
                                        bottomBarViewModel.hideExtension()
                                    }
                                ),
                                EdgeDetectionWidget(
                                    state = cameraTraceViewModel.edgeDetection.collectAsState(),
                                    set = {
                                        cameraTraceViewModel.setEdgeDetection(it)
                                        cameraTraceViewModel.edit()
                                    },
                                    reset = {
                                        cameraTraceViewModel.resetEdgeDetection()
                                        cameraTraceViewModel.edit()
                                    },
                                    close = {
                                        bottomBarViewModel.hideExtension()
                                    }
                                ),
                                ImageSegmentationWidget(
                                    state = cameraTraceViewModel.imageSegmentation.collectAsState(),
                                    set = {
                                        cameraTraceViewModel.setImageSegmentation(it)
                                        cameraTraceViewModel.edit()
                                    },
                                    reset = {
                                        cameraTraceViewModel.resetImageSegmentation()
                                        cameraTraceViewModel.edit()
                                    },
                                    close = {
                                        bottomBarViewModel.hideExtension()
                                    }
                                ),
                                ColourRemoverWidget(
                                    redState = cameraTraceViewModel.red.collectAsState(),
                                    greenState = cameraTraceViewModel.green.collectAsState(),
                                    blueState = cameraTraceViewModel.blue.collectAsState(),
                                    set = { red, green, blue ->
                                        cameraTraceViewModel.setColourRemover(red, green, blue)
                                    },
                                    edit = { red, green, blue ->
                                        cameraTraceViewModel.edit()
                                    },
                                    reset = {
                                        cameraTraceViewModel.resetColourRemover()
                                        cameraTraceViewModel.edit()
                                    },
                                    close = {
                                        bottomBarViewModel.hideExtension()
                                    }
                                ),
                                ColourMergingWidget(
                                    state = cameraTraceViewModel.colourMerging.collectAsState(),
                                    set = {
                                        cameraTraceViewModel.setColourMerging(it)
                                        cameraTraceViewModel.edit()
                                    },
                                    reset = {
                                        cameraTraceViewModel.resetColourMerging()
                                        cameraTraceViewModel.edit()
                                    },
                                    close = {
                                        bottomBarViewModel.hideExtension()
                                    }
                                ),
                                BlurWidget(
                                    state = cameraTraceViewModel.blur.collectAsState(),
                                    set = {
                                        cameraTraceViewModel.setBlur(it)
                                        cameraTraceViewModel.edit()
                                    },
                                    reset = {
                                        cameraTraceViewModel.resetBlur()
                                        cameraTraceViewModel.edit()
                                    },
                                    close = {
                                        bottomBarViewModel.hideExtension()
                                    }
                                ),
                                ContrastWidget(
                                    contrastState = cameraTraceViewModel.contrast.collectAsState(),
                                    brightnessState = cameraTraceViewModel.brightness.collectAsState(),
                                    set = {contrast, brightness ->
                                        cameraTraceViewModel.setContrast(contrast, brightness)
                                    },
                                    edit = {contrast, brightness ->
                                        cameraTraceViewModel.edit()
                                    },
                                    reset = {
                                        cameraTraceViewModel.resetContrast()
                                        cameraTraceViewModel.edit()
                                    },
                                    close = {
                                        bottomBarViewModel.hideExtension()
                                    }
                                ),
                                ColourOverlayWidget(
                                    state = cameraTraceViewModel.colourOverlay.collectAsState(),
                                    set = {
                                        cameraTraceViewModel.setColourOverlay(it)
                                        cameraTraceViewModel.edit()
                                    },
                                    reset = {
                                        cameraTraceViewModel.resetColourOverlay()
                                        cameraTraceViewModel.edit()
                                    },
                                    close = {
                                        bottomBarViewModel.hideExtension()
                                    }
                                ),
                                InvertWidget(
                                    state = cameraTraceViewModel.invert.collectAsState(),
                                    set = {
                                        cameraTraceViewModel.setInvert(it)
                                        cameraTraceViewModel.edit()
                                    },
                                    reset = {
                                        cameraTraceViewModel.resetInvert()
                                        cameraTraceViewModel.edit()
                                    },
                                    close = {
                                        bottomBarViewModel.hideExtension()
                                    }
                                ),
                                MonochromeWidget(
                                    state = cameraTraceViewModel.monochrome.collectAsState(),
                                    set = {
                                        cameraTraceViewModel.setMonochrome(it)
                                        cameraTraceViewModel.edit()
                                    },
                                    reset = {
                                        cameraTraceViewModel.resetMonochrome()
                                        cameraTraceViewModel.edit()
                                    },
                                    close = {
                                        bottomBarViewModel.hideExtension()
                                    }
                                ),
                                BlackAndWhiteWidget(
                                    state = cameraTraceViewModel.blackAndWhite.collectAsState(),
                                    set = {
                                        cameraTraceViewModel.setBlackAndWhite(it)
                                        cameraTraceViewModel.edit()
                                    },
                                    reset = {
                                        cameraTraceViewModel.resetBlackAndWhite()
                                        cameraTraceViewModel.edit()
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

    @Composable
    fun CameraView(isBarVisible : Boolean, cameraTraceViewModel : CameraTraceViewModel) {

        val tracingImage by cameraTraceViewModel.tracingBitmap.collectAsState()

        val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
            var scale = cameraTraceViewModel.scale.value
            scale *= zoomChange
            cameraTraceViewModel.setScale(scale)

            var rotation = cameraTraceViewModel.rotation.value
            rotation += rotationChange
            cameraTraceViewModel.setRotation(rotation)

            var offset = cameraTraceViewModel.offset.value
            offset += offsetChange
            cameraTraceViewModel.setOffset(offset)
        }

        Box(modifier = Modifier.fillMaxSize()) {

            CameraDisplay()

            tracingImage?.let {

                val scale by cameraTraceViewModel.scale.collectAsState()
                val rotation by cameraTraceViewModel.rotation.collectAsState()
                val offset by cameraTraceViewModel.offset.collectAsState()

                val enablePinchToZoom by cameraTraceViewModel.enablePinchToZoom.collectAsState()

                //the user has allowed pinch to zoom and the editing
                //tools are visible so the user is currently editing
                val enabled = isBarVisible && enablePinchToZoom

                Box(modifier = Modifier.fillMaxSize(), contentAlignment =  Alignment.Center) {
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
                            ))
                }
            }

        }

    }
}