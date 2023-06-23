package com.rhuarhri.imagetracer.botton_bar

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.rhuarhri.imagetracer.database.EditingSettingsDao
import com.rhuarhri.imagetracer.database.ImageDao
import javax.inject.Inject

const val DEFAULT_SCALE : Float = 1f
const val DEFAULT_ROTATION : Float = 0f
const val DEFAULT_OFFSET_X : Float = 0f
const val DEFAULT_OFFSET_Y : Float = 0f
const val DEFAULT_ENABLE_PINCH_TO_ZOOM : Boolean = false
const val DEFAULT_INVERT : Boolean = false
const val DEFAULT_RED : Int = 100
const val DEFAULT_GREEN : Int = 100
const val DEFAULT_BlUE : Int = 100
const val DEFAULT_TRANSPARENCY : Int = 100
const val DEFAULT_CONTRAST : Int = 10
const val DEFAULT_BRIGHTNESS : Int = 0
const val DEFAULT_LUMINANCE : Int = 0
const val DEFAULT_MONOCHROME : Boolean = false

data class EditImageSettings(
    var invert : Boolean = DEFAULT_INVERT,
    var red : Int = DEFAULT_RED, //as a percentage
    var green : Int = DEFAULT_GREEN, //as a percentage
    var blue : Int = DEFAULT_BlUE, //as a percentage
    var transparency :  Int = DEFAULT_TRANSPARENCY, //as a percentage
    var contrast : Int = DEFAULT_CONTRAST,
    var brightness : Int = DEFAULT_BRIGHTNESS,
    var luminance : Int = DEFAULT_LUMINANCE,
    var isMonochrome : Boolean = DEFAULT_MONOCHROME
)

class ImageTracingBottomBarRepository @Inject constructor(
    private val imageDao : ImageDao,
    private val editImageSettingsDao: EditingSettingsDao
    ) : ImageTracingBottomBarRepositoryInterface {

    override suspend fun getSettings(): EditImageSettings {
        return editImageSettingsDao.getSettings().map { settings ->
                EditImageSettings(
                    settings.invert,
                    settings.red,
                    settings.green,
                    settings.blue,
                    settings.transparency,
                    settings.contrast,
                    settings.brightness,
                    settings.luminance,
                    settings.isMonochrome
                )
            }.firstOrNull() ?: EditImageSettings()
    }

    private suspend fun updateSettings(setting: EditImageSettings) {

        editImageSettingsDao.updateSettings(
            setting.invert,
            setting.red,
            setting.green,
            setting.blue,
            setting.transparency,
            setting.contrast,
            setting.brightness,
            setting.luminance,
            setting.isMonochrome
        )
    }

    override suspend fun getImage(): Bitmap {
        val imageData = imageDao.getImage().first()
        val imageBytes = imageData.data

        //update settings if there are no settings fro this image
        editImageSettingsDao.checkAndUpdateSettings(imageData.id)

        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    override suspend fun editImage(
        origin : Bitmap,
        setting : EditImageSettings) : Bitmap {
        var editedBitmap = origin

        updateSettings(setting)

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

    private fun setTransparency(transparency : Int, image : Bitmap) : Bitmap {
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

interface ImageTracingBottomBarRepositoryInterface {

    suspend fun getSettings() : EditImageSettings

    suspend fun getImage(): Bitmap

    suspend fun editImage(
        origin : Bitmap,
        setting : EditImageSettings) : Bitmap
}