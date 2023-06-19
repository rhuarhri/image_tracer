package com.rhuarhri.imagetracer.botton_bar

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.Button
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
import com.rhuarhri.imagetracer.popups.HelpPopup

object BottomBar {

    @Composable
    fun Bar(viewModel: BottomBarViewModel, items: Array<BottomBarItem>, extension : @Composable
        (item : BottomBarItem) -> Unit) {

        val isVisible by viewModel.isBarVisible.collectAsState()
        val isExtensionVisible by viewModel.isExtensionVisible.collectAsState()
        val selectedItem by viewModel.selectedItem.collectAsState()

        if (isVisible) {
            Box(
                modifier =
                Modifier
                    .background(MaterialTheme.colorScheme.primary)
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {

                Column() {

                    if (isExtensionVisible) {
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

                                    extension.invoke(it)
                                }
                            }
                        }
                    }

                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        this.items(items) {
                            Item(item = it, onItemClicked = {
                                viewModel.showExtension()
                                viewModel.setSelectedItem(it)
                            })
                        }
                    }
                }
            }
        }

    }

    @Composable
    private fun Item(item : BottomBarItem, onItemClicked: (option: BottomBarItem) -> Unit) {
        Column(
            modifier = Modifier
                .clickable {
                    onItemClicked.invoke(item)
                }
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = stringResource(id = item.title),
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                stringResource(id = item.title),
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 20.sp)
        }
    }

    @Composable
    fun CloseExtensionButton(viewModel: BottomBarViewModel) {
        Button(onClick = {
            viewModel.hideExtension()
        },
            modifier = Modifier.padding(8.dp)
            ) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close",
                modifier = Modifier.size(40.dp, 40.dp), tint = MaterialTheme.colorScheme.onPrimary
                )
        }
    }

    @Composable
    fun RestButton(onRest : () -> Unit) {
        Button(onClick = {
            onRest.invoke()
        },
        modifier = Modifier.padding(8.dp)
            ) {
            Icon(imageVector = Icons.Default.Undo, contentDescription = "Undo",
                modifier = Modifier.size(40.dp, 40.dp), tint = MaterialTheme.colorScheme.onPrimary)
        }
    }

}