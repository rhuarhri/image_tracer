package com.rhuarhri.imagetracer.popups.general

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rhuarhri.imagetracer.R
import com.rhuarhri.imagetracer.popups.HelpPopup

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
                Button(onClick = { onScreenTraceSelected.invoke() }) {
                    Icon(
                        imageVector = Icons.Default.PhoneAndroid,
                        contentDescription = "Screen",
                        modifier = Modifier.size(40.dp))
                }

                Button(onClick = { onCameraTraceSelected.invoke() }) {
                    Icon(
                        imageVector = Icons.Default.Camera,
                        contentDescription = "Camera",
                        modifier = Modifier.size(40.dp))
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