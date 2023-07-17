package com.rhuarhri.imagetracer.editing

import android.graphics.Bitmap
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc

class Sharpen {

    fun set(image : Bitmap, enabled : Boolean) : Bitmap {
        if (!enabled) {
            return image
        }

        val sharpened = Mat()
        val imageMat = Mat()
        Utils.bitmapToMat(image, imageMat)

        val blurred = Mat()
        Imgproc.GaussianBlur(imageMat, blurred, Size(0.0, 0.0), 10.0)
        Core.addWeighted(imageMat, 1.5, blurred, -0.5, 0.0, sharpened)

        val bitmap = EditingUtils.createBlankBitmap(image.width, image.height)
        Utils.matToBitmap(sharpened, bitmap)
        return bitmap
    }
}