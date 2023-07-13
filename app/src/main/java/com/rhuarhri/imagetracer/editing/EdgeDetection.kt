package com.rhuarhri.imagetracer.editing

import android.graphics.Bitmap
import com.rhuarhri.imagetracer.R
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

class EdgeDetection {

    companion object {
        enum class Level(val title : Int) {
            NONE(R.string.none_type),
            LOW(R.string.low_type),
            MEDIUM(R.string.medium_type),
            HIGH(R.string.high_type)
        }
    }

    private val low1 = 275.0
    private val low2 = 300.0

    private val medium1 = 175.0
    private val medium2 = 200.0

    private val high1 = 75.0
    private val high2 = 100.0

    private fun getThreshold(level : Level) : Pair<Double, Double> {
        return when(level) {
            Level.HIGH -> Pair(high1, high2)
            Level.MEDIUM -> Pair(medium1, medium2)
            else -> Pair(low1, low2)
        }
    }

    fun set(image : Bitmap, level : Level) : Bitmap {
        if (level == Level.NONE) {
            return image
        }

        //based on https://www.youtube.com/watch?v=TqNOsD9DWxM
        val imageMat = Mat()
        val edgeMat = Mat()

        Utils.bitmapToMat(image, imageMat)

        //confidence of edge detection is between 80 and 100
        val threshold = getThreshold(level)
        Imgproc.Canny(imageMat, edgeMat, threshold.first, threshold.second)

        Core.bitwise_not(edgeMat, edgeMat)

        val bitmap = EditingUtils.createBlankBitmap(image.width, image.height)
        Utils.matToBitmap(edgeMat, bitmap)
        return EditingUtils.setWhiteToTransparent(bitmap)
    }
}