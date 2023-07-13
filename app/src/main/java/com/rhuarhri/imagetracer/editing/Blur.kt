package com.rhuarhri.imagetracer.editing

import android.graphics.Bitmap
import com.rhuarhri.imagetracer.R
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc

class Blur {

    companion object {
        enum class Level( val title : Int ) {
            NONE(R.string.none_type),
            LOW(R.string.low_type),
            MEDIUM(R.string.medium_type),
            HIGH(R.string.high_type)
        }
    }

    private val LOW = 7.0
    private val MEDIUM = 15.0
    private val HIGH = 21.0

    fun set(image : Bitmap, level : Level) : Bitmap {
        if (level == Level.NONE) {
            return image
        }

        val imageMat = Mat()
        val blurred = Mat()

        Utils.bitmapToMat(image, imageMat)

        val size = getSize(level)
        Imgproc.GaussianBlur(imageMat, blurred, size, 0.0)

        val bitmap = EditingUtils.createBlankBitmap(image.width, image.height)
        Utils.matToBitmap(blurred, bitmap)
        return bitmap
    }

    private fun getSize(level : Level) : Size {
        return when(level) {
            Companion.Level.LOW -> Size(LOW, LOW)
            Companion.Level.MEDIUM -> Size(MEDIUM, MEDIUM)
            else -> Size(HIGH, HIGH)
        }
    }
}