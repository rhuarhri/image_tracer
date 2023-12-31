package com.rhuarhri.imagetracer.botton_bar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rhuarhri.imagetracer.R
import com.rhuarhri.imagetracer.botton_bar.widgets.BottomBarWidget
import com.rhuarhri.imagetracer.popups.HelpPopup

object BottomBar {

    @Composable
    fun Bar(
        viewModel: BottomBarViewModel,
        widgets : List<BottomBarWidget>
    ) {

        val visible by viewModel.isBarVisible.collectAsState()
        val selectedItem by viewModel.selectedItem.collectAsState()

        if (visible) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {

                Column {

                    /*The extension will be controls for that option*/
                    AnimatedVisibility(visible = (selectedItem != null)) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .clip(RoundedCornerShape(16.dp))
                                .background(MaterialTheme.colorScheme.onPrimary)
                        ) {

                            selectedItem?.let {
                                Column(
                                    modifier = Modifier.fillMaxWidth()
                                ) {

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .wrapContentHeight(),
                                        horizontalArrangement = Arrangement.End
                                    ) {

                                        /*In order to help the user if they get stuck there is a
                                        popup for every extension describing what the extension
                                        does and how to use it
                                         */
                                        var showHelpPopup by remember {
                                            mutableStateOf(false)
                                        }

                                        IconButton(
                                            onClick = {
                                                showHelpPopup = true
                                            }, modifier = Modifier
                                                .size(48.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Help,
                                                contentDescription = "help information",
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                        }

                                        if (showHelpPopup) {
                                            HelpPopup(
                                                title = stringResource(id = R.string.help_popup_title),
                                                message = stringResource(id = it.helpMessage)
                                            ) {
                                                showHelpPopup = false
                                            }
                                        }
                                    }

                                    /*This is the extension. Since this is a general bottom bar
                                    the extension is passed in through the extension function.
                                     */
                                    it.Extension()
                                }
                            }
                        }
                    }

                    /*This is a the bottom bar which contains the options.
                     */
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        this.items(widgets) {
                            val selected = selectedItem?.title == it.title

                            Item(item = it, selected, onItemClicked = {
                                viewModel.setSelectedItem(it)
                            })
                        }
                    }
                }
            }
        }

    }

    @Composable
    private fun Item(item : BottomBarWidget, selected : Boolean,
                     onItemClicked: (option: BottomBarWidget) -> Unit) {

        var columnModifier = Modifier.clickable {
            onItemClicked.invoke(item)
        }

        //If selected draw a border around the item to show that this item is currently selected.
        if (selected) {
            columnModifier = columnModifier.border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(8.dp)
            )
        }

        Column(
            modifier = columnModifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = stringResource(id = item.title),
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                stringResource(id = item.title),
                color = MaterialTheme.colorScheme.primary,
                fontSize = 20.sp)
        }
    }

}