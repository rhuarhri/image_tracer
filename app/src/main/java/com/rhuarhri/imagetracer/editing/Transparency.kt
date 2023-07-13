package com.rhuarhri.imagetracer.editing

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import com.rhuarhri.imagetracer.features.DEFAULT_TRANSPARENCY

class Transparency {

    /*
    This will cause the image to fade into the background. 100 visible. 0 invisible.
     */

    fun set(transparency : Int, image : Bitmap) : Bitmap {
        if (transparency == DEFAULT_TRANSPARENCY) {
            return image
        }

        val transparentBitmap = EditingUtils.createBlankBitmap(image.width, image.height)

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
}