package com.rhuarhri.imagetracer.popups

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Help
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HelpPopup(title : String, message : String, onDismiss : () -> Unit) {

    AlertDialog(
        onDismissRequest = { onDismiss.invoke() },
        icon = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    imageVector = Icons.Default.Help,
                    contentDescription = "Help",
                    Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        title = {
            Text(title)
        },
        text = {
            Text(message, fontSize = 18.sp)
        },
        confirmButton = {
            //same as dismiss button
            IconButton(onClick = { onDismiss.invoke() }) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Close popup")
            }
        }
    )
}
