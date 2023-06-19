package com.rhuarhri.imagetracer.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Matrix
import android.graphics.Paint
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

object ImageUtils {

    fun assetFileToBitmap(context: Context, fileName : String) : Bitmap? {
        return try {
            val stream = context.assets.open(fileName)

            BitmapFactory.decodeStream(stream)
        } catch (e : Exception) {
            Log.e("ASSERT TO BITMAP", "Asset file does not exist")
            null
        }
    }



    fun getRGBColour(colour : Int) : Triple<Int, Int, Int> {
        return Triple(
            android.graphics.Color.red(colour),
            android.graphics.Color.green(colour),
            android.graphics.Color.blue(colour)
        )
    }

    fun createBlankBitmap(width : Int, height : Int) : Bitmap {
        return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    }

    fun changeImageColour(
        redPercentage : Int,
        greenPercentage : Int,
        bluePercentage : Int,
        image : Bitmap) : Bitmap {

        val red = ((redPercentage * 255) / 100)
        val green = ((greenPercentage * 255) / 100)
        val blue = ((bluePercentage * 255) / 100)

        val editedBitmap = createBlankBitmap(image.width, image.height)

        (0 until image.width).forEach { columnIndex ->
            (0 until image.height).forEach { rowIndex ->
                val pixel = image.getPixel(columnIndex, rowIndex)

                val matchedColor = if (Color(pixel) == Color.Transparent) {
                    pixel
                } else {
                    val rgb = getRGBColour(pixel)

                    if (rgb.first <= red && rgb.second <= green && rgb.third <= blue) {
                        val newColour = Color(rgb.first, rgb.second, rgb.third,
                            255)
                        newColour.toArgb()
                    } else {
                        android.graphics.Color.TRANSPARENT
                    }
                }

                editedBitmap.setPixel(columnIndex, rowIndex, matchedColor)
            }
        }

        return editedBitmap
    }

    fun setTransparency(transparency : Int, image : Bitmap) : Bitmap {
        val transparentBitmap = createBlankBitmap(image.width, image.height)

        val alpha = ((transparency * 255) / 100)

        val canvas = Canvas(transparentBitmap)
        val paint = Paint()
        paint.alpha = alpha
        canvas.drawBitmap(image, 0f, 0f, paint)

        return transparentBitmap
    }

    fun setRotation(rotation: Int, image: Bitmap): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(rotation.toFloat())

        return Bitmap.createBitmap(
            image, 0, 0,
            image.width,
            image.height,
            matrix, false
        )
    }

    fun resizeImage(size: Int, image: Bitmap): Bitmap {

        var width = image.width + ((image.width * size) / 100)
        var height = image.height + ((image.height * size) / 100)

        if (width < 1) {
            width = 1
        }

        if (height < 1) {
            height = 1
        }

        return Bitmap.createScaledBitmap(image, width, height, false)
    }

    fun setToMonochrome(image : Bitmap) : Bitmap {
        val monochromeBitmap = createBlankBitmap(image.width, image.height)
        val canvas = Canvas(monochromeBitmap)
        val ma = ColorMatrix()
        ma.setSaturation(0f)
        val paint = Paint()
        paint.colorFilter = ColorMatrixColorFilter(ma)
        canvas.drawBitmap(image, 0f, 0f, paint)

        return monochromeBitmap
    }
}