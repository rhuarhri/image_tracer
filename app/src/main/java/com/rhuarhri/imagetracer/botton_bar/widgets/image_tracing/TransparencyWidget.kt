package com.rhuarhri.imagetracer.botton_bar.widgets.image_tracing

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HideImage
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rhuarhri.imagetracer.R
import com.rhuarhri.imagetracer.botton_bar.widgets.BottomBarWidget

class TransparencyWidget (
    private val state : State<Int>,
    private val set : (transparency : Int) -> Unit,
    private val edit : (transparency : Int) -> Unit,
    reset : () -> Unit,
    close : () -> Unit,
) : BottomBarWidget(reset, close) {

    override val title: Int = R.string.image_tracing_transparency_option
    override val icon: ImageVector = Icons.Default.HideImage
    override val helpMessage: Int = R.string.image_tracing_transparency_help

    @Composable
    override fun Extension() {
        ImageTracingExtensionBody(
            height = 150.dp) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                val transparency by state
                BarSlider(
                    title = stringResource(id = title),
                    value = transparency,
                    range = 0f..100f,
                    onValueChanged = {
                        set.invoke(it)
                    },
                    onChangeFinished = {
                        edit.invoke(transparency)
                    }
                )
            }
        }
    }
}