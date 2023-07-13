package com.rhuarhri.imagetracer.editing

import android.graphics.Bitmap

class Invert {

    /*Invert the colours so they become the opposite colour*/

    fun set(image : Bitmap, enabled : Boolean) : Bitmap {
        if (!enabled) {
            return image
        }

        val contrast = -1
        val brightness = 255

        return Contrast().set(image, contrast, brightness)
    }
}