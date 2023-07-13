package com.rhuarhri.imagetracer.editing

import android.graphics.Bitmap
import com.rhuarhri.imagetracer.R
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.Mat

class Flip {

    companion object {
        enum class Type(val title : Int) {
            NONE(R.string.none_type),
            X_AXIS(R.string.flip_x_axis),
            Y_AXIS(R.string.flip_y_axis),
            X_Y_AXIS(R.string.flip_x_y_axis)
        }
    }

    private val X_AXIS = 1
    private val Y_AXIS = 0
    private val X_Y_AXIS = -1

    private fun getFlipCode(type : Type) : Int {
        return when(type) {
            Type.X_AXIS -> X_AXIS
            Type.Y_AXIS -> Y_AXIS
            else -> X_Y_AXIS
        }
    }

    fun set(image : Bitmap, type : Type) : Bitmap {
        if (type == Type.NONE) {
            return image
        }

        val imageMat = Mat()
        Utils.bitmapToMat(image, imageMat)

        /*Flip codes
        1 left right flip
        0 top bottom flip
        -1 top bottom and right left flip
         */

        Core.flip(imageMat, imageMat, getFlipCode(type))

        val bitmap = EditingUtils.createBlankBitmap(image.width, image.height)
        Utils.matToBitmap(imageMat, bitmap)
        return bitmap
    }
}