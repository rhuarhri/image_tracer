package com.rhuarhri.imagetracer.botton_bar


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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Contrast
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rhuarhri.imagetracer.R
import kotlin.math.roundToInt

class ImageTracingBottomBar {

    @Composable
    fun Bar(viewModel: ImageTracingBottomBarViewModel) {
        BottomBar.Bar(viewModel = viewModel as BottomBarViewModel,
            items = EditingFeature.values() as Array<BottomBarItem>) {
            when (it) {
                /*the extension for the bottom bar is chosen here*/
                EditingFeature.Reset -> {
                    Reset(viewModel = viewModel)
                }
                EditingFeature.Resize -> {
                    ResizeImage(viewModel = viewModel)
                }
                EditingFeature.Invert -> {
                    Invert(viewModel = viewModel)
                }
                EditingFeature.ChangeColor -> {
                    ChangeImageColour(viewModel = viewModel)
                }
                EditingFeature.Transparency -> {
                    TransparencyImage(viewModel = viewModel)
                }
                EditingFeature.Contrast -> {
                    Contrast(viewModel = viewModel)
                }
                EditingFeature.Luminance -> {
                    Luminance(viewModel = viewModel)
                }
                EditingFeature.Monochrome -> {
                    Monochrome(viewModel = viewModel)
                }
                else -> {

                }
            }
        }
    }

    @Composable
    fun Reset(viewModel : ImageTracingBottomBarViewModel) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Button(onClick = {
                viewModel.resetAll()
            }) {
                Icon(imageVector = Icons.Default.Undo,
                    contentDescription = stringResource(id = R.string.image_tracing_bar_reset_item_title),
                    modifier = Modifier.size(40.dp, 40.dp))

                Spacer(modifier = Modifier.width(16.dp))

                Text(stringResource(id = R.string.image_tracing_bar_reset_item_title), fontSize = 20.sp)
            }

            BottomBar.CloseExtensionButton(viewModel = viewModel)
        }
    }

    @Composable
    fun ImageTracingExtensionBody(
        height : Dp,
        onReset : () -> Unit,
        viewModel: ImageTracingBottomBarViewModel,
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
                    BottomBar.RestButton {
                        onReset.invoke()
                    }

                    BottomBar.CloseExtensionButton(viewModel = viewModel)
                }
            }

        }
    }

    @Composable
    fun ResizeImage(viewModel: ImageTracingBottomBarViewModel) {
        val enablePinchToZoom by viewModel.enablePinchToZoom.collectAsState()

        ImageTracingExtensionBody(
            height = 150.dp,
            onReset = { viewModel.resetScale()
                viewModel.resetRotation()
                viewModel.resetOffset()
                viewModel.resetEnablePinchToZoom()
            }, viewModel = viewModel) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    stringResource(id = R.string.image_tracing_bar_resize_item_title),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp
                )

                Switch(checked = enablePinchToZoom, onCheckedChange = {
                    viewModel.setEnablePinchToZoom(!enablePinchToZoom)
                })
            }
        }
    }

    @Composable
    fun Invert(viewModel : ImageTracingBottomBarViewModel) {

        ImageTracingExtensionBody(
            height = 150.dp,
            onReset = {
                viewModel.resetInvert()
                viewModel.edit()
            },
            viewModel = viewModel) {
            Button(onClick = {
                val invert = viewModel.invert.value
                viewModel.setInvert(!invert)
                viewModel.edit()
            }) {
                Icon(
                    imageVector = Icons.Default.Contrast,
                    contentDescription = stringResource(id = R.string.image_tracing_bar_invert_item_title),
                    modifier = Modifier.size(40.dp, 40.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    stringResource(id = R.string.image_tracing_bar_invert_item_title),
                    fontSize = 20.sp
                )
            }
        }
    }
    
    @Composable
    fun ChangeImageColour(viewModel : ImageTracingBottomBarViewModel) {
        val red by viewModel.red.collectAsState()
        val green by viewModel.green.collectAsState()
        val blue by viewModel.blue.collectAsState()

        ImageTracingExtensionBody(
            height = 350.dp,
            onReset = {
                viewModel.resetRed()
                viewModel.resetGreen()
                viewModel.resetBlue()
                viewModel.edit()
            }, viewModel = viewModel) {

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                BarSlider(
                    title = stringResource(id = R.string.image_tracing_bar_colour_item_red),
                    value = red,
                    range = 0f..100f,
                    onValueChanged = {
                        viewModel.setRed(it)
                    },
                    onChangeFinished = {
                        viewModel.edit()
                    }
                )

                BarSlider(
                    title = stringResource(id = R.string.image_tracing_bar_colour_item_green),
                    value = green,
                    range = 0f..100f,
                    onValueChanged = {
                        viewModel.setGreen(it)
                    },
                    onChangeFinished = {
                        viewModel.edit()
                    }
                )

                BarSlider(
                    title = stringResource(id = R.string.image_tracing_bar_colour_item_blue),
                    value = blue,
                    range = 0f..100f,
                    onValueChanged = {
                        viewModel.setBlue(it)
                    },
                    onChangeFinished = {
                        viewModel.edit()
                    }
                )
            }

        }
    }
    
    @Composable
    fun TransparencyImage(viewModel: ImageTracingBottomBarViewModel) {

        val transparency by viewModel.transparency.collectAsState()

        ImageTracingExtensionBody(
            height = 150.dp,
            onReset = {
                viewModel.resetTransparency()
                viewModel.edit()
                      },
            viewModel = viewModel) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                BarSlider(
                    title = stringResource(id = R.string.image_tracing_bar_transparency_item_title),
                    value = transparency,
                    range = 0f..100f,
                    onValueChanged = {
                        viewModel.setTransparency(it)
                    },
                    onChangeFinished = {
                        viewModel.edit()
                    }
                )
            }
        }
    }

    @Composable
    fun Contrast(viewModel : ImageTracingBottomBarViewModel) {

        val contrast by viewModel.contrast.collectAsState()
        val brightness by viewModel.brightness.collectAsState()

        ImageTracingExtensionBody(
            height = 250.dp,
            onReset = {
                viewModel.resetContrast()
                viewModel.resetBrightness()
                viewModel.edit()
            },
            viewModel = viewModel) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                BarSlider(
                    title = stringResource(id = R.string.image_tracing_bar_contrast_item_title),
                    value = contrast,
                    range = 10f..30f,
                    onValueChanged = {
                        viewModel.setContrast(it)
                    },
                    onChangeFinished = {
                        viewModel.edit()
                    }
                )

                BarSlider(
                    title = stringResource(id = R.string.image_tracing_bar_brightness_item_title),
                    value = brightness,
                    range = -50f..50f,
                    onValueChanged = {
                        viewModel.setBrightness(it)
                    },
                    onChangeFinished = {
                        viewModel.edit()
                    }
                )
            }
        }
    }

    @Composable
    fun Luminance(viewModel: ImageTracingBottomBarViewModel) {

        val luminance by viewModel.luminance.collectAsState()

        ImageTracingExtensionBody(
            height = 150.dp,
            onReset = {
                viewModel.resetLuminance()
                viewModel.edit()
            },
            viewModel = viewModel) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                BarSlider(
                    title = stringResource(id = R.string.image_tracing_bar_shadows_item_title),
                    value = luminance,
                    range = 0f..100f,
                    onValueChanged = {
                        viewModel.setLuminance(it)
                    },
                    onChangeFinished = {
                        viewModel.edit()
                    }
                )
            }
        }
    }

    @Composable
    fun Monochrome(viewModel : ImageTracingBottomBarViewModel) {

        ImageTracingExtensionBody(
            height = 150.dp,
            onReset = {
                viewModel.resetMonochrome()
                viewModel.edit()
            },
            viewModel = viewModel) {
            Button(onClick = {
                val isMonochrome = viewModel.isMonochrome.value
                viewModel.setMonochrome(!isMonochrome)
                viewModel.edit()
            }) {
                Icon(
                    imageVector = Icons.Default.Contrast,
                    contentDescription = stringResource(id = R.string.image_tracing_bar_monochrome_item_title),
                    modifier = Modifier.size(40.dp, 40.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    stringResource(id = R.string.image_tracing_bar_monochrome_item_title),
                    fontSize = 20.sp
                )
            }
        }
    }

    @Composable
    fun BarSlider(
        title : String,
        value : Int,
        range : ClosedFloatingPointRange<Float>,
        onValueChanged : (value : Int) -> Unit,
        onChangeFinished : () -> Unit
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)) {
            Text(
                stringResource(id = R.string.image_tracing_bar_bar_slider_title, title, value),
                modifier = Modifier.fillMaxWidth(),
                fontSize = 24.sp
            )
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
}