package com.rhuarhri.imagetracer.editing

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint

class Monochrome {

    /*
    Turn the image into a gray scale version of the image.
     */

    fun set(image : Bitmap, enabled : Boolean) : Bitmap {
        if (!enabled) {
            return image
        }

        val monochromeBitmap = EditingUtils.createBlankBitmap(image.width, image.height)
        val canvas = Canvas(monochromeBitmap)
        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(0f)
        val paint = Paint()
        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
        canvas.drawBitmap(image, 0f, 0f, paint)

        return monochromeBitmap
    }
}