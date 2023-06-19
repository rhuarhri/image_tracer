package com.rhuarhri.imagetracer.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.rhuarhri.imagetracer.R
import com.rhuarhri.imagetracer.navigation.Route
import com.rhuarhri.imagetracer.popups.WarningPopup
import com.rhuarhri.imagetracer.popups.ad.FullScreenAdPopup
import com.rhuarhri.imagetracer.popups.general.TracingChoicePopup
import com.rhuarhri.imagetracer.ui.theme.ImageTracerTheme
import com.rhuarhri.imagetracer.utils.ImageUtils
import com.rhuarhri.imagetracer.utils.PermissionUtils

@OptIn(ExperimentalMaterial3Api::class)
class MenuScreen {

    @Composable
    fun Screen(navController: NavController) {

        ImageTracerTheme {
            // A surface container using the 'background' color from the theme
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Scaffold(
                    content = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(it)
                        ) {
                            Column(modifier = Modifier.fillMaxSize()) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.Center
                                ) {

                                    val icon =
                                        ImageUtils.assetFileToBitmap(
                                            LocalContext.current,
                                            "app_icon.png"
                                        )
                                    icon?.let { bitmap ->
                                        Image(
                                            bitmap = bitmap.asImageBitmap(),
                                            contentDescription = "App icon",
                                            modifier = Modifier
                                                .size(150.dp)
                                                .clip(RoundedCornerShape(16.dp))
                                                .background(MaterialTheme.colorScheme.primary),
                                            contentScale = ContentScale.Fit
                                        )
                                    }
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(stringResource(id = R.string.app_name), fontSize = 36.sp)
                                }

                                Menu(navController)

                            }
                        }
                    },
                    bottomBar = {
                        BannerAd()
                    }
                )
            }
        }
    }

    @Composable
    private fun Menu(navController: NavController) {
        val viewModel: MenuViewModel = hiltViewModel()

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                var showCanTraceWarning by remember {
                    mutableStateOf(false)
                }

                if (showCanTraceWarning) {
                    WarningPopup(
                        title =
                        stringResource(id = R.string.can_not_trace_warning_popup_title),
                        message =
                        stringResource(id = R.string.can_not_trace_warning_popup_message)
                    ) {
                        showCanTraceWarning = false
                    }
                }

                MenuButton(
                    icon = Icons.Default.PlayArrow,
                    description = stringResource(id = R.string.start_button_content_description)
                ) {
                    val canTrace = viewModel.canTraceImage()
                    if (canTrace) {
                        viewModel.increaseAdCount()
                        navController.navigate(Route.ImageSelectionScreen.route)
                    } else {
                        showCanTraceWarning = true
                    }
                }

                var showTracingChoicePopup by remember {
                    mutableStateOf(false)
                }
                
                var showContinueWarningPopup by remember {
                    mutableStateOf(false)
                }

                val imagesExist by viewModel.imagesExist.collectAsState()

                if (imagesExist) {
                    if (showTracingChoicePopup) {
                        val context = LocalContext.current
                        TracingChoicePopup(
                            onScreenTraceSelected = {
                                navController.navigate(Route.LightBoxScreen.route)
                            },
                            onCameraTraceSelected = {
                                if (PermissionUtils.hasCameraPermission(context)) {
                                    navController.navigate(Route.CameraTraceScreen.route)
                                }
                            },
                            onDismiss = {
                                showTracingChoicePopup = false
                            }
                        )
                    }
                } else {
                    if (showContinueWarningPopup) {
                        WarningPopup(
                            title = stringResource(id = R.string.no_continue_warning_title),
                            message = stringResource(id = R.string.no_continue_warning_message)) {
                            showContinueWarningPopup = false
                        }
                    }
                }

                MenuButton(
                    icon = Icons.Default.Restore,
                    description = stringResource(id = R.string.continue_button_content_description)
                ) {
                    showTracingChoicePopup = true
                    showContinueWarningPopup = true
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {

                MenuButton(
                    icon = Icons.Default.Help,
                    description = stringResource(id = R.string.help_button_content_description)
                ) {

                }
            }

            WatchAd(viewModel)

        }
    }

    @Composable
    fun WatchAd(viewModel: MenuViewModel) {
        val adCountList by viewModel.adCount.collectAsState()
        val adCount = if (adCountList.isEmpty()) {
            0
        } else {
            adCountList.first()
        }

        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    stringResource(id = R.string.ad_count_title, adCount),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    fontSize = 36.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    stringResource(id = R.string.ad_card_message),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.size(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    val context = LocalContext.current

                    var showNoConnectionWarningPopup by remember {
                        mutableStateOf(false)
                    }

                    if (showNoConnectionWarningPopup) {
                        WarningPopup(
                            title =
                            stringResource(id = R.string.no_connection_warning_popup_title),
                            message =
                            stringResource(id = R.string.no_connection_warning_popup_message)
                        ) {
                            showNoConnectionWarningPopup = false
                        }
                    }

                    var showFullScreenAd by remember {
                        mutableStateOf(false)
                    }

                    if (showFullScreenAd) {
                        FullScreenAdPopup {
                            showFullScreenAd = false
                        }
                    }

                    Button(onClick = {
                        val connection = viewModel.checkConnection(context)

                        if (connection) {
                            showFullScreenAd = true
                            viewModel.resetAdCount()
                        } else {
                            showNoConnectionWarningPopup = true
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "watch ad",
                            modifier = Modifier.size(40.dp),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            stringResource(id = R.string.watch_ad_button),
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 24.sp
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun BannerAd() {
        Box(
            modifier =
            Modifier
                .background(MaterialTheme.colorScheme.primary)
                .fillMaxWidth()
                .height(100.dp)
                .padding(8.dp)
        ) {
            Text("Banner ad here")
        }
    }

    @Composable
    fun MenuButton(icon : ImageVector, description : String, onClick : () -> Unit) {
        Button(onClick = { onClick.invoke() },
            modifier = Modifier.size(150.dp, 100.dp))
        {
            Icon(
                imageVector = icon,
                contentDescription = description,
                modifier = Modifier.size(60.dp, 60.dp)
            )
        }
    }
}

