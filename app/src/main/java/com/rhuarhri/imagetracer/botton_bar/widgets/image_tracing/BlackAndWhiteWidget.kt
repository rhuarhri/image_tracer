package com.rhuarhri.imagetracer.botton_bar.widgets.image_tracing

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterBAndW
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rhuarhri.imagetracer.R
import com.rhuarhri.imagetracer.botton_bar.widgets.BottomBarWidget

class BlackAndWhiteWidget (
    private val state : State<Boolean>,
    private val set : (enabled : Boolean) -> Unit,
    reset : () -> Unit,
    close : () -> Unit,
) : BottomBarWidget(reset, close) {

    override val title: Int = R.string.image_tracing_black_and_white_option
    override val icon: ImageVector = Icons.Default.FilterBAndW
    override val helpMessage: Int = R.string.image_tracing_black_and_white_help

    @Composable
    override fun Extension() {
        val enabled by state

        ImageTracingExtensionBody(
            height = 150.dp) {
            Button(onClick = {
                set.invoke(!enabled)
            }) {
                Icon(
                    imageVector = icon,
                    contentDescription = stringResource(id = title),
                    modifier = Modifier.size(40.dp, 40.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    stringResource(id = title),
                    fontSize = 20.sp
                )
            }
        }
    }
}