package com.rhuarhri.imagetracer.popups.general

import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.rhuarhri.imagetracer.R
import com.rhuarhri.imagetracer.popups.HelpPopup
import com.rhuarhri.imagetracer.popups.WarningPopup

@Composable
fun TracingChoicePopup(
    onScreenTraceSelected: () -> Unit,
    onCameraTraceSelected: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss.invoke() },
        icon = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {

                var showHelpPopup by remember {
                    mutableStateOf(false)
                }

                IconButton(onClick = {
                    showHelpPopup = true
                }) {
                    Icon(
                        imageVector = Icons.Default.Help,
                        contentDescription = "Help",
                        tint = MaterialTheme.colorScheme.primary)
                }

                if (showHelpPopup) {
                    HelpPopup(
                        title = stringResource(id = R.string.help_popup_title),
                        message = stringResource(id = R.string.tracing_choice_popup_help)
                    ) {
                        showHelpPopup = false
                    }
                }
            }
        },
        title = {
            Text(stringResource(id = R.string.tracing_choice_popup_title))
        },
        text = {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {

                ScreenChoiceButton {
                    onScreenTraceSelected.invoke()
                    onDismiss.invoke()
                }

                CameraChoiceButton {
                    onCameraTraceSelected.invoke()
                    onDismiss.invoke()
                }

            }
        },
        confirmButton = {
            //same as dismiss button
            IconButton(onClick = { onDismiss.invoke() }) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Close popup")
            }
        }
    )
}

@Composable
fun ScreenChoiceButton(onClick : () -> Unit) {

    Button(onClick = { onClick.invoke() }) {
        Icon(
            imageVector = Icons.Default.PhoneAndroid,
            contentDescription = "Screen",
            modifier = Modifier.size(40.dp))
    }
}

@Composable
fun CameraChoiceButton(onClick: () -> Unit) {
    var showPermissionDeniedPopup by remember {
        mutableStateOf(false)
    }

    if (showPermissionDeniedPopup) {
        WarningPopup(
            title = stringResource(id = R.string.tracing_choice_no_permission_warning_title),
            message = stringResource(id = R.string.tracing_choice_no_permission_warning_message)) {
            showPermissionDeniedPopup = false
        }
    }

    var isPermissionGranted by remember {
        mutableStateOf(false)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        isPermissionGranted = isGranted
        if (!isGranted) {
            showPermissionDeniedPopup = true
        }
    }

    val context = LocalContext.current

    Button(onClick = {

        val permission = android.Manifest.permission.CAMERA

        isPermissionGranted = ContextCompat.checkSelfPermission(
            context, permission) == PackageManager.PERMISSION_GRANTED

        if (!isPermissionGranted) {
            permissionLauncher.launch(permission)
        }

        if (isPermissionGranted) {
            onClick.invoke()
        }
    }) {
        Icon(
            imageVector = Icons.Default.Camera,
            contentDescription = "Camera",
            modifier = Modifier.size(40.dp))
    }
}
