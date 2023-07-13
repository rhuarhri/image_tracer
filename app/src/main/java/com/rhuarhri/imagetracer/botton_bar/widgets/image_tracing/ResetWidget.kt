package com.rhuarhri.imagetracer.botton_bar.widgets.image_tracing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rhuarhri.imagetracer.R
import com.rhuarhri.imagetracer.botton_bar.widgets.BottomBarWidget

class ResetWidget(
    private val reset : () -> Unit,
    close : () -> Unit,
) : BottomBarWidget(reset, close) {

    override val title: Int = R.string.image_tracing_reset_option
    override val icon: ImageVector = Icons.Default.Undo
    override val helpMessage: Int = R.string.image_tracing_reset_help

    @Composable
    override fun Extension() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Button(onClick = {
                reset.invoke()
            }) {
                Icon(imageVector = icon,
                    contentDescription = stringResource(id = title),
                    modifier = Modifier.size(40.dp, 40.dp))

                Spacer(modifier = Modifier.width(16.dp))

                Text(stringResource(id = title), fontSize = 20.sp)
            }

            CloseExtensionButton()
        }
    }
}