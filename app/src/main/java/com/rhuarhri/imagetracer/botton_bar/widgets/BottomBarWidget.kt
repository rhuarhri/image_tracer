package com.rhuarhri.imagetracer.botton_bar.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

abstract class BottomBarWidget (
    private val reset : () -> Unit,
    private val close : () -> Unit
) {

    abstract val title: Int
    abstract val icon: ImageVector
    abstract val helpMessage: Int

    @Composable
    abstract fun Extension()

    @Composable
    protected fun ImageTracingExtensionBody(
        height : Dp,
        body : @Composable (scope : ColumnScope) -> Unit) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(height),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f, true), horizontalAlignment = Alignment.CenterHorizontally) {

                body.invoke(this)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    RestButton()

                    CloseExtensionButton()
                }
            }

        }
    }

    @Composable
    protected fun CloseExtensionButton() {
        Button(onClick = {
            close.invoke()
        },
            modifier = Modifier.padding(8.dp)
        ) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close",
                modifier = Modifier.size(40.dp, 40.dp), tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }

    @Composable
    protected fun RestButton() {
        /*This app allows you to edit as a result this is a general undo button to correct
        mistakes.*/

        Button(onClick = {
            reset.invoke()
        },
            modifier = Modifier.padding(8.dp)
        ) {
            Icon(imageVector = Icons.Default.Undo, contentDescription = "Undo",
                modifier = Modifier.size(40.dp, 40.dp), tint = MaterialTheme.colorScheme.onPrimary)
        }
    }

    @Composable
    protected fun BarSlider(
        title : String,
        value : Int,
        range : ClosedFloatingPointRange<Float>,
        onValueChanged : (value : Int) -> Unit,
        onChangeFinished : () -> Unit
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                title,
                modifier = Modifier.wrapContentWidth(),
                fontSize = 20.sp
            )
            Spacer(Modifier.width(8.dp))
            Slider(
                value = value.toFloat(),
                onValueChange = {
                    onValueChanged.invoke(it.roundToInt())
                },
                onValueChangeFinished = {
                    onChangeFinished.invoke()
                },
                valueRange = range
            )
        }
    }

    @Composable
    protected fun OptionSelection (options : List<String>, selected : Int, onSelection : (index : Int) -> Unit) {
        LazyRow(modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)
            .selectableGroup(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            items(options) {
                val index = options.indexOf(it)
                Row(modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    val isChecked = selected == index

                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = {
                            onSelection.invoke(index)
                        },
                        enabled = true,
                    )
                    Text(text = it)
                }
            }
        }
    }
}