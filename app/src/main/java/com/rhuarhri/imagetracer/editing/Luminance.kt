package com.rhuarhri.imagetracer.editing

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color

class Luminance {

    /*
    This high lights dark spots in the app. This could be useful.
     */


    fun set(image : Bitmap, luminancePercentage : Int) : Bitmap {
        if (luminancePercentage == 0) {
            return image
        }

        val luminance = luminancePercentage / 100f

        val editedBitmap = EditingUtils.createBlankBitmap(image.width, image.height)

        val pixels = IntArray(image.height * image.width)

        image.getPixels(
            pixels,
            0,
            image.width,
            0,
            0,
            image.width,
            image.height
        )

        val defaultColour = android.graphics.Color.WHITE
        pixels.forEachIndexed { index, pixel ->
            val matchedColor =
                if (Color(pixel) == Color.Transparent || Color(pixel) == Color.White) {
                    defaultColour
                } else {
                    if (android.graphics.Color.luminance(pixel) > luminance) {
                        //bright color
                        defaultColour
                    } else {
                        android.graphics.Color.BLACK
                    }
                }
            pixels[index] = matchedColor
        }

        editedBitmap.setPixels(pixels, 0, image.width, 0, 0, image.width, image.height)

        return editedBitmap
    }
}