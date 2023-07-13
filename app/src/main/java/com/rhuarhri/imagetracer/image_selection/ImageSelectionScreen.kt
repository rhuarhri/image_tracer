package com.rhuarhri.imagetracer.image_selection

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.rhuarhri.imagetracer.R
import com.rhuarhri.imagetracer.botton_bar.BottomBar
import com.rhuarhri.imagetracer.botton_bar.BottomBarViewModel
import com.rhuarhri.imagetracer.botton_bar.widgets.image_selection.CameraWidget
import com.rhuarhri.imagetracer.botton_bar.widgets.image_selection.HistoryWidget
import com.rhuarhri.imagetracer.botton_bar.widgets.image_selection.InternetWidget
import com.rhuarhri.imagetracer.botton_bar.widgets.image_selection.SampleWidget
import com.rhuarhri.imagetracer.botton_bar.widgets.image_selection.StorageWidget
import com.rhuarhri.imagetracer.navigation.Route
import com.rhuarhri.imagetracer.popups.LoadingPopup
import com.rhuarhri.imagetracer.popups.WarningPopup
import com.rhuarhri.imagetracer.popups.general.TracingChoicePopup
import com.rhuarhri.imagetracer.ui.theme.ImageTracerTheme

class ImageSelectionScreen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Screen(navController : NavController) {

        val viewModel : ImageSelectionViewModel = hiltViewModel()

        val bottomBarViewModel : BottomBarViewModel = androidx.lifecycle.viewmodel.compose.viewModel()

        val selectedImage by viewModel.selectedBitmap.collectAsState()

        ImageTracerTheme {
            // A surface container using the 'background' color from the theme
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Scaffold(
                    content = {
                        Box(modifier = Modifier
                            .fillMaxSize()
                            .padding(it)) {
                            selectedImage?.let { image ->
                                Image(bitmap = image.asImageBitmap(),
                                    contentDescription = "selected image",
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    },
                    floatingActionButton = {

                        var showTracingChoicePopup by remember {
                            mutableStateOf(false)
                        }

                        if (showTracingChoicePopup) {
                            TracingChoicePopup(
                                onScreenTraceSelected = {
                                    navController.navigate(Route.LightBoxScreen.route){
                                        popUpTo(Route.MenuScreen.route)
                                    }
                                },
                                onCameraTraceSelected = {
                                    navController.navigate(Route.CameraTraceScreen.route){
                                        popUpTo(Route.MenuScreen.route)
                                    }
                                },
                                onDismiss = {
                                    showTracingChoicePopup = false
                                }
                            )
                        }

                        var showNoImageSelectedWarning by remember {
                            mutableStateOf(false)
                        }
                        
                        if (showNoImageSelectedWarning) {
                            WarningPopup(
                                title =
                                stringResource(
                                    id = R.string.image_selection_no_image_selected_warning_title
                                ),
                                message = stringResource(
                                    id = R.string.image_selection_no_image_selected_warning_message
                                )) {
                                showNoImageSelectedWarning = false
                            }
                        }

                        var showLoadingPopup by remember {
                            mutableStateOf(false)
                        }

                        if (showLoadingPopup) {
                            LoadingPopup(stringResource(id = R.string.loading_popup_content))
                        }

                        val context = LocalContext.current
                        FloatingActionButton(onClick = {
                            if (selectedImage != null) {
                                selectedImage?.let {
                                    showLoadingPopup = true
                                    viewModel.saveImage(
                                        context
                                    ).invokeOnCompletion {
                                        showLoadingPopup = false
                                        showTracingChoicePopup = true
                                    }
                                }
                            } else {
                                showNoImageSelectedWarning = true
                            }
                        }) {
                            Icon(imageVector = Icons.Default.ArrowForward,
                                contentDescription = "Next")
                        }
                    },
                    bottomBar = {

                        /*It is possible for errors to occur here. Mainly no internet or incorrect url
                        so if there is a problem this warning is shown.
                        */

                        val internetError by viewModel.showInternetError.collectAsState()
                        if (internetError) {
                            WarningPopup(
                                title = stringResource(id = R.string.image_selection_internet_error_title),
                                message = stringResource(id = R.string.image_selection_internet_error_message)
                            ) {
                                viewModel.hideInternetError()
                            }
                        }

                        val history by viewModel.history.collectAsState()
                        BottomBar.Bar(viewModel = bottomBarViewModel, widgets = listOf(
                            StorageWidget(
                                set = { context, uri ->
                                    viewModel.getFromStorage(context, uri)
                                },
                                reset = {

                                },
                                close = {
                                    bottomBarViewModel.hideExtension()
                                }
                            ),
                            CameraWidget(
                                set = { context, uri ->
                                    viewModel.getFromCamera(context, uri)
                                },
                                reset = {

                                },
                                close = {
                                    bottomBarViewModel.hideExtension()
                                }
                            ),
                            InternetWidget(
                                set = {
                                    viewModel.getFromInternet(it)
                                },
                                reset = {

                                },
                                close = {
                                    bottomBarViewModel.hideExtension()
                                }
                            ),
                            SampleWidget(
                                set = {
                                    viewModel.setSelectedBitmap(it)
                                },
                                reset = {

                                },
                                close = {
                                    bottomBarViewModel.hideExtension()
                                }
                            ),
                            HistoryWidget(
                                historyImages = history,
                                set = {
                                    viewModel.setSelectedBitmap(it)
                                },
                                reset = {

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