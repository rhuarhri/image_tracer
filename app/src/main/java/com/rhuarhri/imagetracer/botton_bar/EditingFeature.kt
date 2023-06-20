package com.rhuarhri.imagetracer.botton_bar

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Contrast
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Exposure
import androidx.compose.material.icons.filled.HideImage
import androidx.compose.material.icons.filled.InvertColors
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Pinch
import androidx.compose.material.icons.filled.Undo
import androidx.compose.ui.graphics.vector.ImageVector
import com.rhuarhri.imagetracer.R

enum class EditingFeature(
    @StringRes override val title : Int,
    override val icon : ImageVector,
    @StringRes override val helpMessage: Int
) : BottomBarItem {
    Reset(R.string.image_tracing_reset_option,
        Icons.Default.Undo,
        R.string.image_tracing_reset_help),

    Resize(R.string.image_tracing_resize_option,
        Icons.Default.Pinch,
        R.string.image_tracing_resize_help),

    Invert(R.string.image_tracing_invert_option,
        Icons.Default.InvertColors,
        R.string.image_tracing_invert_help),

    ChangeColor(R.string.image_tracing_colour_option,
        Icons.Default.Palette,
        R.string.image_tracing_colour_help),

    Transparency(R.string.image_tracing_transparency_option,
        Icons.Default.HideImage,
        R.string.image_tracing_transparency_help),

    Contrast(R.string.image_tracing_contrast_option,
        Icons.Default.Exposure,
        R.string.image_tracing_contrast_help),

    Luminance(R.string.image_tracing_shadow_option,
        Icons.Default.DarkMode,
        R.string.image_tracing_shadow_help),

    Monochrome(R.string.image_tracing_monochrome_option,
        Icons.Default.Contrast,
        R.string.image_tracing_monochrome_help)
}