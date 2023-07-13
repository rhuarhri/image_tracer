package com.rhuarhri.imagetracer.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rhuarhri.imagetracer.features.DEFAULT_BLACK_AND_WHITE
import com.rhuarhri.imagetracer.features.DEFAULT_BLUR
import com.rhuarhri.imagetracer.features.DEFAULT_BRIGHTNESS
import com.rhuarhri.imagetracer.features.DEFAULT_BlUE
import com.rhuarhri.imagetracer.features.DEFAULT_COLOUR_MERGING
import com.rhuarhri.imagetracer.features.DEFAULT_COLOUR_OVERLAY
import com.rhuarhri.imagetracer.features.DEFAULT_CONTRAST
import com.rhuarhri.imagetracer.features.DEFAULT_EDGE_DETECTION
import com.rhuarhri.imagetracer.features.DEFAULT_FLIP
import com.rhuarhri.imagetracer.features.DEFAULT_GREEN
import com.rhuarhri.imagetracer.features.DEFAULT_IMAGE_SEGMENTATION
import com.rhuarhri.imagetracer.features.DEFAULT_INVERT
import com.rhuarhri.imagetracer.features.DEFAULT_MONOCHROME
import com.rhuarhri.imagetracer.features.DEFAULT_RED
import com.rhuarhri.imagetracer.features.DEFAULT_TRANSPARENCY

@Entity(tableName = "settings_table")
data class EditingSettings(
    @PrimaryKey
    val id : Int = 0,
    val imageId : Int,
    var blackAndWhite : Boolean = DEFAULT_BLACK_AND_WHITE,
    var blur : Int = DEFAULT_BLUR.ordinal,
    var colourMerging : Int = DEFAULT_COLOUR_MERGING.ordinal,
    var colourOverlay : Int = DEFAULT_COLOUR_OVERLAY.ordinal,
    var red : Int = DEFAULT_RED, //as a percentage
    var green : Int = DEFAULT_GREEN, //as a percentage
    var blue : Int = DEFAULT_BlUE, //as a percentage
    var contrast : Int = DEFAULT_CONTRAST,
    var brightness : Int = DEFAULT_BRIGHTNESS,
    var edgeDetection : Int = DEFAULT_EDGE_DETECTION.ordinal,
    var flip : Int = DEFAULT_FLIP.ordinal,
    var imageSegmentation : Boolean = DEFAULT_IMAGE_SEGMENTATION,
    var invert : Boolean = DEFAULT_INVERT,
    var isMonochrome : Boolean = DEFAULT_MONOCHROME,
    var transparency : Int = DEFAULT_TRANSPARENCY, //as a percentage
)