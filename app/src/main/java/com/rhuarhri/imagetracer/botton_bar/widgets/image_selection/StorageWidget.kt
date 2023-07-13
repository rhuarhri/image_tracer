package com.rhuarhri.imagetracer.botton_bar.widgets.image_selection

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rhuarhri.imagetracer.R
import com.rhuarhri.imagetracer.botton_bar.widgets.BottomBarWidget

class StorageWidget (
    private val set : (context : Context, uri : Uri) -> Unit,
    reset : () -> Unit,
    close : () -> Unit,
) : BottomBarWidget(reset, close) {

    override val title: Int = R.string.image_selection_storage_option
    override val icon: ImageVector = Icons.Default.Folder
    override val helpMessage: Int = R.string.image_selection_storage_help

    private val galleryButtonTitle : Int = R.string.image_selection_bar_storage_item_button_title

    @Composable
    override fun Extension() {
        /*This should show a the devices own image picker. So the user can choose from all of
        there images.*/

        val context: Context = LocalContext.current

        val pickMedia = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri ->
                if (uri != null) {
                    set.invoke(context, uri)
                } else {
                    //No media selected

                }
            })

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }) {
                Icon(
                    imageVector = Icons.Default.Folder, contentDescription = "Storage",
                    modifier = Modifier.size(40.dp, 40.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    stringResource(id = galleryButtonTitle),
                    fontSize = 20.sp)
            }

            CloseExtensionButton()
        }
    }
}