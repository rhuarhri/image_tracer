package com.rhuarhri.imagetracer.popups

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.AlertDialog
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.rhuarhri.imagetracer.R

@Composable
fun LoadingPopup(loadingMessage : String) {
    AlertDialog(
        onDismissRequest = { },
        modifier = Modifier
            .width(250.dp)
            .padding(16.dp),
        title = {
            Text(
                stringResource(id = R.string.loading_popup_title),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center)
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
                Text(loadingMessage,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,)
            }
        },
        confirmButton = {

        },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    )
}