package com.rhuarhri.imagetracer.botton_bar

import androidx.compose.ui.graphics.vector.ImageVector

interface BottomBarItem {
    val title : Int
    val icon : ImageVector
    val helpMessage : Int
}