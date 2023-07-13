package com.rhuarhri.imagetracer.botton_bar.widgets.image_tracing

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Palette
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rhuarhri.imagetracer.R
import com.rhuarhri.imagetracer.botton_bar.widgets.BottomBarWidget

class ColourRemoverWidget (
    private val redState : State<Int>,
    private val greenState : State<Int>,
    private val blueState : State<Int>,
    private val set : (red : Int, green : Int, blue : Int) -> Unit,
    private val edit : (red : Int, green : Int, blue : Int) -> Unit,
    reset : () -> Unit,
    close : () -> Unit,
) : BottomBarWidget(reset, close) {

    override val title: Int = R.string.image_tracing_colour_option
    override val icon: ImageVector = Icons.Default.Palette
    override val helpMessage: Int = R.string.image_tracing_colour_help

    private val redTitle : Int = R.string.image_tracing_bar_colour_item_red
    private val greenTitle : Int = R.string.image_tracing_bar_colour_item_green
    private val blueTitle : Int = R.string.image_tracing_bar_colour_item_blue

    @Composable
    override fun Extension() {

        ImageTracingExtensionBody(
            height = 220.dp) {

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                val red by redState
                val green by greenState
                val blue by blueState
                BarSlider(
                    title = stringResource(id = redTitle),
                    value = red,
                    range = 0f..100f,
                    onValueChanged = {
                        set.invoke(it, green, blue)
                    },
                    onChangeFinished = {
                        edit.invoke(red, green, blue)
                    }
                )

                BarSlider(
                    title = stringResource(id = greenTitle),
                    value = green,
                    range = 0f..100f,
                    onValueChanged = {
                        set.invoke(red, it, blue)
                    },
                    onChangeFinished = {
                        edit.invoke(red, green, blue)
                    }
                )

                BarSlider(
                    title = stringResource(id = blueTitle),
                    value = blue,
                    range = 0f..100f,
                    onValueChanged = {
                        set.invoke(red, green, it)
                    },
                    onChangeFinished = {
                        edit.invoke(red, green, blue)
                    }
                )
            }

        }
    }
}