package com.rhuarhri.imagetracer.botton_bar


import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.rhuarhri.imagetracer.R
import com.rhuarhri.imagetracer.popups.WarningPopup
import com.rhuarhri.imagetracer.utils.ImageUtils
import com.rhuarhri.imagetracer.values.SAMPLE_IMAGES
import java.io.File
import java.util.Objects

object ImageSelectionBottomBar {

    @Composable
    fun Bar(viewModel: ImageSelectionBottomBarViewModel) {
        BottomBar.Bar(
            viewModel = viewModel as BottomBarViewModel,
            items = ImageSelectionOption.values() as Array<BottomBarItem>
        ) {
            when (it) {
                /*When the extension is visible. The extension that will be displayed is chosen
                here
                 */
                ImageSelectionOption.Storage -> {
                    StorageImages(viewModel)
                }

                ImageSelectionOption.Camera -> {
                    CameraImages(viewModel)
                }

                ImageSelectionOption.Internet -> {
                    InternetImages(viewModel)
                }

                ImageSelectionOption.Sample -> {
                    SampleImages(viewModel)
                }
                ImageSelectionOption.History -> {
                    HistoryImages(viewModel)
                }
                else -> {

                }
            }
        }
    }

    @Composable
    fun StorageImages(viewModel: ImageSelectionBottomBarViewModel) {

        /*This should show a the devices own image picker. So the user can choose from all of
        there images.*/

        val context: Context = LocalContext.current

        val pickMedia = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri ->
                if (uri != null) {
                    viewModel.getFromStorage(context, uri)
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
                    stringResource(id = R.string.image_selection_bar_storage_item_button_title),
                    fontSize = 20.sp)
            }

            BottomBar.CloseExtensionButton(viewModel)
        }
    }

    @Composable
    fun CameraImages(viewModel: ImageSelectionBottomBarViewModel) {
        val context = LocalContext.current
        val file = File.createTempFile("temp", ".png", context.externalCacheDir)
        val uri = FileProvider.getUriForFile(
            Objects.requireNonNull(context),
            context.packageName + ".provider", file
        )

        /*this should launch the devices camera app from which they can take a picture. It is
        storage in a temp file.
         */

        val cameraLauncher =
            rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
                viewModel.getFromCamera(context, uri)
            }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = {
                cameraLauncher.launch(uri)
            }) {
                Icon(
                    imageVector = Icons.Default.Camera, contentDescription = "Camera",
                    modifier = Modifier.size(40.dp, 40.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    stringResource(id = R.string.image_selection_bar_camera_item_button_title),
                    fontSize = 20.sp)
            }
            BottomBar.CloseExtensionButton(viewModel)
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun InternetImages(viewModel: ImageSelectionBottomBarViewModel) {
        var url by remember { mutableStateOf("") }

        /*This allows the user to get an image from a url*/

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = url,
                onValueChange = {
                    url = it
                },
                modifier = Modifier
                    .weight(1f, true)
                    .padding(8.dp),
                label = {
                    Text(stringResource(id = R.string.image_selection_bar_internet_item_search_title))
                }
            )
            IconButton(onClick = {
                viewModel.getFromInternet(url)
            }) {
                Icon(
                    imageVector = Icons.Default.Search, contentDescription = "Search",
                    modifier = Modifier.size(40.dp, 40.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            BottomBar.CloseExtensionButton(viewModel)

            val internetError by viewModel.showInternetError.collectAsState()

            /*It is possible for errors to occur here. Mainly no internet or incorrect url
            so if there is a problem this warning is shown.
             */

            if (internetError) {
                WarningPopup(
                    title =
                    stringResource(id = R.string.image_selection_internet_error_title),
                    message =
                    stringResource(id = R.string.image_selection_internet_error_message)
                ) {
                    viewModel.seenError()
                }
            }
        }
    }

    @Composable
    fun SampleImages(viewModel: ImageSelectionBottomBarViewModel) {

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
                                    viewModel.setSelectedBitmap(bitmap)
                                }
                        )
                    }
                }
            }
            BottomBar.CloseExtensionButton(viewModel)
        }
    }

    @Composable
    fun HistoryImages(viewModel: ImageSelectionBottomBarViewModel) {

        /*this displays the first 10 images in the app image history
        * The history contains the images that the user has traced in the past.*/

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val historyImages by viewModel.imageHistory.collectAsState()
            LazyRow(modifier = Modifier.weight(1f, fill = true)) {
                items(historyImages) {bitmap ->

                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Selected image",
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable {
                                viewModel.setSelectedBitmap(bitmap)
                            }
                    )

                }
            }
            BottomBar.CloseExtensionButton(viewModel)
        }
    }
}

