package com.rhuarhri.imagetracer.botton_bar

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.rhuarhri.imagetracer.database.ImageDao
import javax.inject.Inject

data class EditImageSettings(
    var transparency :  Int = 100, //as a percentage
    var invert : Boolean = false,
    var red : Int = 100, //as a percentage
    var green : Int = 100, //as a percentage
    var blue : Int = 100, //as a percentage
    var contrast : Int = 1,
    var brightness : Int = 0,
    var luminance : Int = 100,
    var isMonochrome : Boolean = false
)

class ImageTracingBottomBarRepository @Inject constructor(private val imageDao : ImageDao) {

    suspend fun getImage(): Bitmap {
        val imageData = imageDao.getImage().first()
        val imageBytes = imageData.data

        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    suspend fun editImage(
        origin : Bitmap,
        setting : EditImageSettings) : Bitmap {
        var editedBitmap = origin

        if (setting.invert) {
            editedBitmap = invert(editedBitmap)
        }

        editedBitmap = contrast(editedBitmap, setting.contrast, setting.brightness)

        editedBitmap = changeImageColour(setting.red, setting.green, setting.blue, editedBitmap)

        if (setting.isMonochrome) {
            editedBitmap = setToMonochrome(editedBitmap)
        }

        editedBitmap = luminance(editedBitmap, setting.luminance)

        editedBitmap = setTransparency(setting.transparency, editedBitmap)

        return editedBitmap
    }

    private fun getRGBColour(colour : Int) : Triple<Int, Int, Int> {
        return Triple(
            android.graphics.Color.red(colour),
            android.graphics.Color.green(colour),
            android.graphics.Color.blue(colour)
        )
    }

    private fun createBlankBitmap(width : Int, height : Int) : Bitmap {
        return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    }

    private fun changeImageColour(
        redPercentage : Int,
        greenPercentage : Int,
        bluePercentage : Int,
        image : Bitmap
    ) : Bitmap {

        val red = ((redPercentage * 255) / 100)
        val green = ((greenPercentage * 255) / 100)
        val blue = ((bluePercentage * 255) / 100)

        if (red == 255 && green == 255 && blue == 255) {
            //this would not change the image
            return image
        }

        val editedBitmap = createBlankBitmap(image.width, image.height)

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
            pixels[index] = matchedColor
        }

        editedBitmap.setPixels(pixels, 0, image.width, 0, 0, image.width, image.height)

        return editedBitmap
    }

    private fun luminance(image : Bitmap, luminancePercentage : Int) : Bitmap {

        if (luminancePercentage == 0) {
            return image
        }

        val luminance = luminancePercentage / 100f

        val editedBitmap = createBlankBitmap(image.width, image.height)

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

    private fun contrast(image : Bitmap, contrastPercentage: Int, brightnessPercentage: Int) : Bitmap {
        //contrast 0..10 1 is default
        //brightness -255..255 0 is default

        val contrast = ((contrastPercentage * 10) / 100f)
        val brightness = ((brightnessPercentage * 255) / 100f)

        return editWithColorMatrix(image, getContrastColorMatrix(contrast, brightness ))
    }

    private fun invert(image : Bitmap) : Bitmap {
        //contrast 0..10 1 is default
        //brightness -255..255 0 is default

        val contrast = -1
        val brightness = 255

        return editWithColorMatrix(image, getContrastColorMatrix(contrast.toFloat(), brightness.toFloat()))
    }

    private fun getContrastColorMatrix(contrast: Float, brightness: Float) : ColorMatrix {
        return ColorMatrix(
            floatArrayOf(
                contrast, 0f, 0f, 0f, brightness,
                0f, contrast, 0f, 0f, brightness,
                0f, 0f, contrast, 0f, brightness,
                0f, 0f, 0f, 1f, 0f
            )
        )
    }

    private fun editWithColorMatrix(image : Bitmap, colorMatrix: ColorMatrix) : Bitmap {
        val editedBitmap = createBlankBitmap(image.width, image.height)

        val canvas = Canvas(editedBitmap)
        val paint = Paint()
        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
        canvas.drawBitmap(image, 0f, 0f, paint)
        return editedBitmap
    }

    fun setTransparency(transparency : Int, image : Bitmap) : Bitmap {
        val transparentBitmap = createBlankBitmap(image.width, image.height)

        val alpha = ((transparency * 255) / 100)

        if (alpha == 255) {
            //this would not change the image
            return image
        }

        val canvas = Canvas(transparentBitmap)
        val paint = Paint()
        paint.alpha = alpha
        canvas.drawBitmap(image, 0f, 0f, paint)

        return transparentBitmap
    }

    private fun setToMonochrome(image : Bitmap) : Bitmap {
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