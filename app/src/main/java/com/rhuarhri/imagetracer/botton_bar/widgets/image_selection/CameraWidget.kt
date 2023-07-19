package com.rhuarhri.imagetracer.botton_bar.widgets.image_selection

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.rhuarhri.imagetracer.R
import com.rhuarhri.imagetracer.botton_bar.widgets.BottomBarWidget
import com.rhuarhri.imagetracer.popups.WarningPopup
import java.io.File
import java.util.Objects

class CameraWidget (
    private val set : (context : Context, uri : Uri) -> Unit,
    reset : () -> Unit,
    close : () -> Unit,
) : BottomBarWidget(reset, close) {

    override val title: Int = R.string.image_selection_camera_option
    override val icon: ImageVector = Icons.Default.Camera
    override val helpMessage: Int = R.string.image_selection_camera_help

    private val cameraButtonTitle : Int = R.string.image_selection_bar_camera_item_button_title

    private val permissionWarningTitle : Int = R.string.tracing_choice_no_permission_warning_title
    private val permissionWarningMessage : Int = R.string.tracing_choice_no_permission_warning_message

    @Composable
    override fun Extension() {
        val context = LocalContext.current
        val file = File(context.externalCacheDir, "temp.png")

        val uri = FileProvider.getUriForFile(
            Objects.requireNonNull(context),
            context.packageName + ".provider", file
        )

        /*this should launch the devices camera app from which they can take a picture. It is
    storage in a temp file.
    */

        val cameraLauncher =
            rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
                set.invoke(context, uri)
            }

        var showPermissionDeniedPopup by remember {
            mutableStateOf(false)
        }

        if (showPermissionDeniedPopup) {
            WarningPopup(
                title = stringResource(id = permissionWarningTitle),
                message = stringResource(id = permissionWarningMessage)
            ) {
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = {

                val permission = android.Manifest.permission.CAMERA

                isPermissionGranted = ContextCompat.checkSelfPermission(
                    context, permission
                ) == PackageManager.PERMISSION_GRANTED

                if (!isPermissionGranted) {
                    permissionLauncher.launch(permission)
                }

                if (isPermissionGranted) {
                    cameraLauncher.launch(uri)
                }
            }) {
                Icon(
                    imageVector = Icons.Default.Camera, contentDescription = stringResource(id = cameraButtonTitle),
                    modifier = Modifier.size(40.dp, 40.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    stringResource(id = cameraButtonTitle),
                    fontSize = 20.sp
                )
            }
            CloseExtensionButton()
        }
    }
}