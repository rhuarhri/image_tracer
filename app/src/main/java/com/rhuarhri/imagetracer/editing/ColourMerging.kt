package com.rhuarhri.imagetracer.editing

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.rhuarhri.imagetracer.R
import kotlin.math.roundToInt

class ColourMerging {

    /*
    This reduces the verity of colours that are in an image.
    The idea behind this is. If there are less colours in an image parts of the image merge
    together into blobs. This can help be hiding complexity in the image.
    For example your are not trying to draw a detailed picture of a tree. Instead you are getting a
    easier to draw tree shaped blob.
     */

    companion object {
        enum class Level(val title : Int){
            NONE(R.string.none_type),
            LOW(R.string.low_type),
            MEDIUM(R.string.medium_type),
            HIGH(R.string.high_type)
        }
    }

    fun set(image : Bitmap, level : Level) : Bitmap {
        if (level == Level.NONE) {
            return image
        }

        //val blurred = blur(image)

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

            val rgb = EditingUtils.getRGBColour(pixel)

            val red = reduceColours(rgb.first, level)
            val green = reduceColours(rgb.second, level)
            val blue = reduceColours(rgb.third, level)

            val matchedColor = Color(
                red,
                green,
                blue,
                android.graphics.Color.alpha(pixel)
            )
            pixels[index] = matchedColor.toArgb()
        }

        val editedBitmap = EditingUtils.createBlankBitmap(image.width, image.height)
        editedBitmap.setPixels(pixels, 0, image.width, 0, 0, image.width, image.height)

        return editedBitmap
    }

    /*private fun blur(image: Bitmap) : Bitmap {
        //This is to help reduce the verity of colors in the image
        return Blur().set(image, Blur.Companion.Level.NONE)
    }*/

    private val LOW = 8
    private val MEDIUM = 4
    private val HIGH = 2

    private fun reduceColours(value : Int, level : Level) : Int {
        /*
        How it works
        The total colour range is 0 to 255.
        To reduce the colours. This range is split into sections.
        If a colour fits into one of these sections then that colour is reset
        to that sections colour.
         */

        //the number of sections
        val separator = getSeparator(level)

        //the colour range that section handles
        val division = 255.0 / separator
        var newColour = (division * 1).roundToInt()
        for(i in (1 .. separator)) {
            if (value < (division * i)) {
                newColour = (division * i).roundToInt()
                break
            }
        }
        return newColour
    }

    private fun getSeparator(level : Level) : Int {
        return when(level) {
            Level.LOW -> LOW
            Level.MEDIUM -> MEDIUM
            else -> HIGH
        }
    }
}