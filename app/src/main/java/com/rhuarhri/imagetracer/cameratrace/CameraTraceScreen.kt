package com.rhuarhri.imagetracer.cameratrace

import androidx.compose.foundation.Image
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.rhuarhri.imagetracer.botton_bar.ImageTracingBottomBar
import com.rhuarhri.imagetracer.botton_bar.ImageTracingBottomBarViewModel
import com.rhuarhri.imagetracer.ui.theme.ImageTracerTheme

class CameraTraceScreen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Screen(navController : NavController) {

        val barViewModel : ImageTracingBottomBarViewModel = hiltViewModel()

        barViewModel.getImage()

        ImageTracerTheme {
            // A surface container using the 'background' color from the theme
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                    Scaffold(
                        content = {
                            Box(modifier = Modifier.padding(it)) {
                                CameraView(barViewModel)
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

    @Composable
    fun CameraView(barViewModel : ImageTracingBottomBarViewModel) {

        val tracingImage by barViewModel.tracingBitmap.collectAsState()

        Box(modifier = Modifier.fillMaxSize()) {

            CameraDisplay()

            tracingImage?.let {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment =  Alignment.Center) {
                    Image(bitmap = it.asImageBitmap(), contentDescription = "Image being traced",)
                }
            }

        }

    }


}