package com.rhuarhri.imagetracer.botton_bar.widgets.image_tracing

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Exposure
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rhuarhri.imagetracer.R
import com.rhuarhri.imagetracer.botton_bar.widgets.BottomBarWidget

class ContrastWidget (
    private val contrastState : State<Int>,
    private val brightnessState : State<Int>,
    private val set : (contrast: Int, brightness: Int) -> Unit,
    private val edit : (contrast: Int, brightness: Int) -> Unit,
    reset : () -> Unit,
    close : () -> Unit,
) : BottomBarWidget(reset, close) {

    override val title: Int = R.string.image_tracing_contrast_option
    override val icon: ImageVector = Icons.Default.Exposure
    override val helpMessage: Int = R.string.image_tracing_contrast_help

    private val contrastTitle : Int = R.string.image_tracing_bar_contrast_item_title
    private val brightnessTitle : Int = R.string.image_tracing_bar_brightness_item_title

    @Composable
    override fun Extension() {
        ImageTracingExtensionBody(
            height = 180.dp) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                val contrast by contrastState
                val brightness by brightnessState
                BarSlider(
                    title = stringResource(id = contrastTitle),
                    value = contrast,
                    range = 10f..30f,
                    onValueChanged = {
                        set.invoke(it, brightness)
                    },
                    onChangeFinished = {
                        edit.invoke(contrast, brightness)
                    }
                )

                BarSlider(
                    title = stringResource(id = brightnessTitle),
                    value = brightness,
                    range = -50f..50f,
                    onValueChanged = {
                        set.invoke(contrast, it)
                    },
                    onChangeFinished = {
                        edit.invoke(contrast, brightness)
                    }
                )
            }
        }
    }
}