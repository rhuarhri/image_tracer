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
import androidx.compose.material.icons.filled.Restore
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.rhuarhri.imagetracer.R
import com.rhuarhri.imagetracer.botton_bar.widgets.BottomBarWidget
import com.rhuarhri.imagetracer.utils.ImageUtils

class HistoryWidget (
    private val historyImages : List<String>,
    private val set : (bitmap : Bitmap) -> Unit,
    reset : () -> Unit,
    close : () -> Unit,
) : BottomBarWidget(reset, close) {

    override val title: Int = R.string.image_selection_history_option
    override val icon: ImageVector = Icons.Default.Restore
    override val helpMessage: Int = R.string.image_selection_history_help

    @Composable
    override fun Extension() {
        /*this displays the first 10 images in the app image history
        * The history contains the images that the user has traced in the past.*/

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            LazyRow(modifier = Modifier.weight(1f, fill = true)) {
                items(historyImages) {filePath ->

                    val thumbNail = ImageUtils.getThumbNail(filePath)

                    thumbNail?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = "Selected image",
                            modifier = Modifier
                                .padding(16.dp)
                                .clickable {
                                    ImageUtils
                                        .getImageFromFile(filePath)
                                        ?.let { bitmap ->
                                            set.invoke(bitmap)
                                        }
                                }
                        )
                    }

                }
            }
            CloseExtensionButton()
        }

    }
}