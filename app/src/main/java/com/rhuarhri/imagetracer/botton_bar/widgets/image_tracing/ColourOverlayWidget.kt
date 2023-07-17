package com.rhuarhri.imagetracer.botton_bar.widgets.image_tracing

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Layers
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
import com.rhuarhri.imagetracer.editing.ColourOverlay

class ColourOverlayWidget (
    private val state : State<ColourOverlay.Companion.Type>,
    private val set : (type : ColourOverlay.Companion.Type) -> Unit,
    reset : () -> Unit,
    close : () -> Unit,
) : BottomBarWidget(reset, close) {

    override val title: Int = R.string.image_tracing_colour_overlay_option
    override val icon: ImageVector = Icons.Default.Layers
    override val helpMessage: Int = R.string.image_tracing_colour_overlay_help

    @Composable
    override fun Extension() {
        ImageTracingExtensionBody(
            height = 200.dp) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val type by state
                OptionSelection(
                    ColourOverlay.Companion.Type.values().map { stringResource(id = it.title) },
                    type.ordinal
                ) {
                    val type = ColourOverlay.Companion.Type.values()[it]
                    set.invoke(type)
                }
            }
        }
    }
}