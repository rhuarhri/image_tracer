package com.rhuarhri.imagetracer.editing

import android.graphics.Bitmap
import com.rhuarhri.imagetracer.R
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.CvType
import org.opencv.core.Mat

class ColourOverlay {

    /*This adds an coloured overlay to the image. The overlay would make the entire image that colour.
    The idea is that someone (maybe some who is colour blind) may find it easier to look at a coloured
    image.
     */

    companion object {
        enum class Type( val title : Int ) {
            NONE(R.string.none_type),
            RED(R.string.colour_overlay_red),
            GREEN(R.string.colour_overlay_green),
            BLUE(R.string.colour_overlay_blue)
        }
    }

    fun set(image : Bitmap, type : Type) : Bitmap {
        if (type == Type.NONE) {
            return  image
        }

        val imageMat = Mat()
        Utils.bitmapToMat(image, imageMat)
        Core.transform(imageMat, imageMat, getMask(type))
        val bitmap = EditingUtils.createBlankBitmap(image.width, image.height)
        Utils.matToBitmap(imageMat, bitmap)
        return bitmap
    }

    private fun getMask(type : Type) : Mat {
        return when(type) {
            Type.RED -> getRedMask()
            Type.GREEN -> getGreenMask()
            else -> getBlueMask()
        }
    }

    private fun getRedMask() : Mat {
        val red = Mat(4, 4, CvType.CV_32F)
        red.put(0, 0,  /* R */0.6, 0.6, 0.6, 0.0)
        red.put(1, 0,  /* G */0.2, 0.2, 0.2, 0.0)
        red.put(2, 0,  /* B */0.2, 0.2, 0.2, 0.0)
        red.put(3, 0,  /* A */0.0, 0.0, 0.0, 1.0)
        return red
    }

    private fun getGreenMask() : Mat {
        val green = Mat(4, 4, CvType.CV_32F)
        green.put(0, 0,  /* R */0.2, 0.2, 0.2, 0.0)
        green.put(1, 0,  /* G */0.6, 0.6, 0.6, 0.0)
        green.put(2, 0,  /* B */0.2, 0.2, 0.2, 0.0)
        green.put(3, 0,  /* A */0.0, 0.0, 0.0, 1.0)
        return green
    }

    private fun getBlueMask() : Mat {
        val blue = Mat(4, 4, CvType.CV_32F)
        blue.put(0, 0,  /* R */0.2, 0.2, 0.2, 0.0)
        blue.put(1, 0,  /* G */0.2, 0.2, 0.2, 0.0)
        blue.put(2, 0,  /* B */0.6, 0.6, 0.6, 0.0)
        blue.put(3, 0,  /* A */0.0, 0.0, 0.0, 1.0)
        return blue
    }
}