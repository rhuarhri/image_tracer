package com.rhuarhri.imagetracer.botton_bar.widgets.image_tracing

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BlurOn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rhuarhri.imagetracer.R
import com.rhuarhri.imagetracer.botton_bar.widgets.BottomBarWidget
import com.rhuarhri.imagetracer.editing.Blur

class BlurWidget (
    private val state : State<Blur.Companion.Level>,
    private val set : (level : Blur.Companion.Level) -> Unit,
    reset : () -> Unit,
    close : () -> Unit,
) : BottomBarWidget(reset, close) {

    override val title: Int = R.string.image_tracing_blur_option
    override val icon: ImageVector = Icons.Default.BlurOn
    override val helpMessage: Int = R.string.image_tracing_blur_help

    @Composable
    override fun Extension() {
        ImageTracingExtensionBody(
            height = 200.dp) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val level by state
                OptionSelection(
                    Blur.Companion.Level.values().map { stringResource(id = it.title) },
                    level.ordinal
                ) {
                    val level = Blur.Companion.Level.values()[it]
                    set.invoke(level)
                }
            }
        }
    }
}