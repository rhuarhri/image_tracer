package com.rhuarhri.imagetracer.popups.ad


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.rhuarhri.imagetracer.R

@Composable
fun FullScreenAdPopup(onDismiss : () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss.invoke() },
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        title = {
            Text(stringResource(id = R.string.full_screen_ad_popup_title))
        },
        text = {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Ad here")
            }
        },
        confirmButton = {
            //same as dismiss button
            IconButton(onClick = { onDismiss.invoke() }) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Close popup")
            }
        },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false)
    )
}
