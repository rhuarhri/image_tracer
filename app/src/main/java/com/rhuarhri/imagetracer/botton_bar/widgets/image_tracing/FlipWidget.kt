package com.rhuarhri.imagetracer.botton_bar.widgets.image_tracing

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flip
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
import com.rhuarhri.imagetracer.editing.Flip

class FlipWidget(
    private val state : State<Flip.Companion.Type>,
    private val set : (type : Flip.Companion.Type) -> Unit,
    reset : () -> Unit,
    close : () -> Unit,
) : BottomBarWidget(reset, close) {

    override val title: Int = R.string.image_tracing_flip_option
    override val icon: ImageVector = Icons.Default.Flip
    override val helpMessage: Int = R.string.image_tracing_flip_help

    @Composable
    override fun Extension() {
        ImageTracingExtensionBody(
            height = 140.dp) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val type by state
                OptionSelection(
                    com.rhuarhri.imagetracer.editing.Flip.Companion.Type.values().map {
                        stringResource(id = it.title)
                    },
                    type.ordinal
                ) {
                    val type = Flip.Companion.Type.values()[it]
                    set.invoke(type)
                }
            }
        }
    }

}