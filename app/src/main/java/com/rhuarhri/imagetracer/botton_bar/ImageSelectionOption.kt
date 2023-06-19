package com.rhuarhri.imagetracer.botton_bar

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.ImageSearch
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.ui.graphics.vector.ImageVector
import com.rhuarhri.imagetracer.R

enum class ImageSelectionOption(
    @StringRes override val title : Int,
    override val icon : ImageVector,
    @StringRes override val helpMessage: Int,
) : BottomBarItem {
    Storage(
        R.string.image_selection_storage_option,
        Icons.Default.Folder,
        R.string.image_selection_storage_help),

    Camera(
        R.string.image_selection_camera_option,
        Icons.Default.Camera,
        R.string.image_selection_camera_help),

    Internet(
        R.string.image_selection_internet_option,
        Icons.Default.Wifi,
        R.string.image_selection_internet_help),

    Sample(
        R.string.image_selection_sample_option,
        Icons.Default.ImageSearch,
        R.string.image_selection_sample_help)
}