package com.rhuarhri.imagetracer.features

import com.rhuarhri.imagetracer.editing.Blur
import com.rhuarhri.imagetracer.editing.ColourMerging
import com.rhuarhri.imagetracer.editing.ColourOverlay
import com.rhuarhri.imagetracer.editing.EdgeDetection
import com.rhuarhri.imagetracer.editing.Flip

//Add any new features here first as the interfaces will highlight any part of the app that needs changing

const val DEFAULT_SCALE : Float = 1f
const val DEFAULT_ROTATION : Float = 0f
const val DEFAULT_OFFSET_X : Float = 0f
const val DEFAULT_OFFSET_Y : Float = 0f
const val DEFAULT_BLACK_AND_WHITE : Boolean = false
val DEFAULT_BLUR : Blur.Companion.Level = Blur.Companion.Level.NONE
val DEFAULT_COLOUR_MERGING : ColourMerging.Companion.Level = ColourMerging.Companion.Level.NONE
val DEFAULT_COLOUR_OVERLAY : ColourOverlay.Companion.Type = ColourOverlay.Companion.Type.NONE
const val DEFAULT_RED : Int = 100
const val DEFAULT_GREEN : Int = 100
const val DEFAULT_BlUE : Int = 100
const val DEFAULT_CONTRAST : Int = 10
const val DEFAULT_BRIGHTNESS : Int = 0
val DEFAULT_EDGE_DETECTION : EdgeDetection.Companion.Level = EdgeDetection.Companion.Level.NONE
val DEFAULT_FLIP : Flip.Companion.Type = Flip.Companion.Type.NONE
const val DEFAULT_IMAGE_SEGMENTATION : Boolean = false
const val DEFAULT_INVERT : Boolean = false
const val DEFAULT_MONOCHROME : Boolean = false
const val DEFAULT_TRANSPARENCY : Int = 100
const val DEFAULT_ENABLE_PINCH_TO_ZOOM : Boolean = false
data class EditingSettings(
    var blackAndWhite: Boolean = DEFAULT_BLACK_AND_WHITE,

    var blur : Blur.Companion.Level = DEFAULT_BLUR,

    var colourMerging: ColourMerging.Companion.Level = DEFAULT_COLOUR_MERGING,

    var colourOverlay : ColourOverlay.Companion.Type = DEFAULT_COLOUR_OVERLAY,

    //for colour remover
    var red : Int = DEFAULT_RED, //as a percentage
    var green : Int = DEFAULT_GREEN, //as a percentage
    var blue : Int = DEFAULT_BlUE, //as a percentage

    //for contrast
    var contrast : Int = DEFAULT_CONTRAST,
    var brightness : Int = DEFAULT_BRIGHTNESS,

    var edgeDetection: EdgeDetection.Companion.Level = DEFAULT_EDGE_DETECTION,

    var flip : Flip.Companion.Type = DEFAULT_FLIP,

    var imageSegmentation : Boolean = DEFAULT_IMAGE_SEGMENTATION,

    var invert : Boolean = DEFAULT_INVERT,

    var isMonochrome : Boolean = DEFAULT_MONOCHROME,

    var transparency :  Int = DEFAULT_TRANSPARENCY //as a percentage
)