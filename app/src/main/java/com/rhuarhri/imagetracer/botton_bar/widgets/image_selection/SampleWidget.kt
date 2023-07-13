package com.rhuarhri.imagetracer.botton_bar.widgets.image_selection

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ImageSearch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.rhuarhri.imagetracer.R
import com.rhuarhri.imagetracer.botton_bar.widgets.BottomBarWidget
import com.rhuarhri.imagetracer.utils.ImageUtils
import com.rhuarhri.imagetracer.values.SAMPLE_IMAGES

class SampleWidget (
    private val set : (bitmap : Bitmap) -> Unit,
    reset : () -> Unit,
    close : () -> Unit,
) : BottomBarWidget(reset, close) {

    override val title: Int = R.string.image_selection_sample_option
    override val icon: ImageVector = Icons.Default.ImageSearch
    override val helpMessage: Int = R.string.image_selection_sample_help

    @Composable
    override fun Extension() {
        /*This will display a list of images. These images in the asset folder and are used as
        example images that the user can choose from.
        All sample image locations are in SAMPLE_IMAGES
        */

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LazyRow(modifier = Modifier.weight(1f, fill = true)) {
                items(SAMPLE_IMAGES) { fileName ->
                    val context = LocalContext.current
                    val bitmap = ImageUtils.assetFileToBitmap(context, fileName)

                    bitmap?.let {
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "Selected image",
                            modifier = Modifier
                                .padding(16.dp)
                                .clickable {
                                    set.invoke(bitmap)
                                }
                        )
                    }
                }
            }
            CloseExtensionButton()
        }
    }
}