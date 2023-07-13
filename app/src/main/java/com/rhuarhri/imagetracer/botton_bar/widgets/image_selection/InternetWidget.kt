package com.rhuarhri.imagetracer.botton_bar.widgets.image_selection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rhuarhri.imagetracer.R
import com.rhuarhri.imagetracer.botton_bar.widgets.BottomBarWidget
import com.rhuarhri.imagetracer.popups.LoadingPopup
import kotlinx.coroutines.Job

class InternetWidget (
    private val set : (url : String) -> Job,
    reset : () -> Unit,
    close : () -> Unit,
) : BottomBarWidget(reset, close) {

    override val title: Int = R.string.image_selection_internet_option
    override val icon: ImageVector = Icons.Default.Wifi
    override val helpMessage: Int = R.string.image_selection_internet_help

    private val searchBarTitle : Int = R.string.image_selection_bar_internet_item_search_title

    private val loadingPopupMessage : Int = R.string.loading_popup_content

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Extension() {

        /*This allows the user to get an image from a url*/
        var url by remember { mutableStateOf("") }

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
                    Text(stringResource(id = searchBarTitle))
                }
            )

            var showLoadingPopup by remember {
                mutableStateOf(false)
            }

            if (showLoadingPopup) {
                LoadingPopup(loadingMessage = stringResource(id = loadingPopupMessage))
            }

            IconButton(onClick = {
                showLoadingPopup = true
                set.invoke(url).invokeOnCompletion {
                    showLoadingPopup = false
                }
            }) {
                Icon(
                    imageVector = Icons.Default.Search, contentDescription = "Search",
                    modifier = Modifier.size(40.dp, 40.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            CloseExtensionButton()
        }

    }
}