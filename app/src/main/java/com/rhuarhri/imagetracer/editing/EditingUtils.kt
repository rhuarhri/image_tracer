package com.rhuarhri.imagetracer.editing

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

object EditingUtils {

    fun createBlankBitmap(width : Int, height : Int) : Bitmap {
        return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    }

    fun getRGBColour(colour : Int) : Triple<Int, Int, Int> {
        return Triple(
            android.graphics.Color.red(colour),
            android.graphics.Color.green(colour),
            android.graphics.Color.blue(colour)
        )
    }

    fun setWhiteToTransparent(image : Bitmap) : Bitmap {
        return setTransparency(image, getRGBColour(android.graphics.Color.WHITE))
    }

    fun setBlackToTransparent(image : Bitmap) : Bitmap {
        return setTransparency(image, getRGBColour(android.graphics.Color.BLACK))
    }

    /*this function is used to convert white pixel to transparent.
    * This is because after editing white parts are often not needed.*/
    private fun setTransparency(image : Bitmap, colour : Triple<Int, Int, Int>) : Bitmap {
        val pixels = IntArray(image.height * image.width)

        /*for more info go to
        https://developer.android.com/reference/android/graphics/Bitmap
         */
        image.getPixels(
            pixels, //The array to receive the bitmap's colors This value cannot be null.
            0, //The first index to write into pixels[]
            image.width, //The number of entries in pixels[] to skip between rows
            //(must be >= bitmap's width). Can be negative.
            0, //The x coordinate of the first pixel to read from the bitmap
            0, //The y coordinate of the first pixel to read from the bitmap
            image.width, //The number of pixels to read from each row
            image.height //The number of rows to read
        )

        pixels.forEachIndexed { index, pixel ->

            val rgb = getRGBColour(pixel)

            val alpha = if (
                rgb.first == colour.first &&
                rgb.second == colour.second &&
                rgb.third == colour.third) {
                0
            } else {
                android.graphics.Color.alpha(pixel)
            }

            val newColour = Color(
                rgb.first,
                rgb.second,
                rgb.third,
                alpha
            )

            pixels[index] = newColour.toArgb()
        }

        val editedBitmap = createBlankBitmap(image.width, image.height)
        editedBitmap.setPixels(pixels, 0, image.width, 0, 0, image.width, image.height)

        return editedBitmap
    }

}