package com.rhuarhri.imagetracer.editing

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import com.rhuarhri.imagetracer.features.DEFAULT_BRIGHTNESS
import com.rhuarhri.imagetracer.features.DEFAULT_CONTRAST

class Contrast {

    /*set the image contrast and brightness*/

    fun setPercentage(image : Bitmap, contrastPercentage: Int, brightnessPercentage: Int) : Bitmap {
        //contrast 0..10 1 is default
        //brightness -255..255 0 is default

        if (contrastPercentage == DEFAULT_CONTRAST && brightnessPercentage == DEFAULT_BRIGHTNESS) {
            return image
        }

        val contrast = ((contrastPercentage * 10) / 100f)
        val brightness = ((brightnessPercentage * 255) / 100f)

        return editWithColorMatrix(image, getContrastColorMatrix(contrast, brightness))
    }

    fun set(image : Bitmap, contrast: Int, brightness: Int) : Bitmap {
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
        val editedBitmap = EditingUtils.createBlankBitmap(image.width, image.height)

        val canvas = Canvas(editedBitmap)
        val paint = Paint()
        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
        canvas.drawBitmap(image, 0f, 0f, paint)
        return editedBitmap
    }
}