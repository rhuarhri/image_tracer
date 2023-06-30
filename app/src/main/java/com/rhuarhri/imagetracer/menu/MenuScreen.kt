package com.rhuarhri.imagetracer.menu

import android.content.res.Configuration
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.rhuarhri.imagetracer.R
import com.rhuarhri.imagetracer.navigation.Route
import com.rhuarhri.imagetracer.popups.LoadingPopup
import com.rhuarhri.imagetracer.popups.WarningPopup
import com.rhuarhri.imagetracer.popups.general.TracingChoicePopup
import com.rhuarhri.imagetracer.ui.theme.ImageTracerTheme
import com.rhuarhri.imagetracer.utils.AdUtils
import com.rhuarhri.imagetracer.utils.ImageUtils

@OptIn(ExperimentalMaterial3Api::class)
class MenuScreen {

    @Composable
    fun Screen(navController: NavController) {

        val config = LocalConfiguration.current

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

                                    if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
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
                                                    .size(80.dp)
                                                    .clip(RoundedCornerShape(16.dp))
                                                    .background(MaterialTheme.colorScheme.primary),
                                                contentScale = ContentScale.Fit
                                            )
                                        }
                                    }
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(stringResource(id = R.string.app_name), fontSize = 24.sp)
                                }

                                Menu(navController)

                            }
                        }
                    },
                    bottomBar = {
                        if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
                            BannerAd()
                        }
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

                /*This checks if the user has used the app before to trace an image. If so then
                they can simple press this button and continue from were they left off.
                 */
                if (imagesExist) {
                    if (showTracingChoicePopup) {
                        TracingChoicePopup(
                            onScreenTraceSelected = {
                                navController.navigate(Route.LightBoxScreen.route)
                            },
                            onCameraTraceSelected = {
                                navController.navigate(Route.CameraTraceScreen.route)
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

        val config = LocalConfiguration.current

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
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center
                )
                if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        stringResource(id = R.string.ad_card_message),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.size(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    val context = LocalContext.current

                    var showAdErrorWarningPopup by remember {
                        mutableStateOf(false)
                    }

                    if (showAdErrorWarningPopup) {
                        WarningPopup(
                            title =
                            stringResource(id = R.string.on_ad_error_popup_title),
                            message =
                            stringResource(id = R.string.on_ad_error_popup_message),
                        ) {
                            showAdErrorWarningPopup = false
                        }
                    }

                    var showLoadingPopup by remember {
                        mutableStateOf(false)
                    }

                    if (showLoadingPopup) {
                        LoadingPopup(loadingMessage = stringResource(id = R.string.loading_ad_popup_content))
                    }

                    Button(onClick = {
                        showLoadingPopup = true
                        AdUtils.showFullScreenAd(context,
                            onDismiss = {
                                showLoadingPopup = false
                                viewModel.resetAdCount()
                            },
                            onError = {
                                showLoadingPopup = false
                                showAdErrorWarningPopup = true
                            }
                        )
                    }) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "watch ad",
                            modifier = Modifier.size(30.dp),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            stringResource(id = R.string.watch_ad_button),
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 18.sp
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
            AdUtils.BannerAd()
        }
    }

    @Composable
    fun MenuButton(icon : ImageVector, description : String, onClick : () -> Unit) {
        Button(onClick = { onClick.invoke() },
            modifier = Modifier.size(90.dp, 60.dp))
        {
            Icon(
                imageVector = icon,
                contentDescription = description,
                modifier = Modifier.size(50.dp, 50.dp)
            )
        }
    }
}

