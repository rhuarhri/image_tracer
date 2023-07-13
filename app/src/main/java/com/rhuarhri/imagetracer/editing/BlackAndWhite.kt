package com.rhuarhri.imagetracer.editing

import android.graphics.Bitmap
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

class BlackAndWhite {

    /*Change the image colour to just black and white*/

    fun set(image : Bitmap, enabled : Boolean) : Bitmap {
        if (!enabled) {
            return image
        }

        val imageMat = Mat()
        val gray = Mat()
        Utils.bitmapToMat(image, imageMat)
        Imgproc.cvtColor(imageMat, gray, Imgproc.COLOR_BGR2GRAY)

        val binaryMat = Mat()
        Imgproc.threshold(gray, binaryMat, 0.0, 255.0, Imgproc.THRESH_BINARY_INV + Imgproc.THRESH_OTSU)

        Core.bitwise_not(binaryMat, binaryMat)

        val bitmap = EditingUtils.createBlankBitmap(image.width, image.height)
        Utils.matToBitmap(binaryMat, bitmap)
        return bitmap
    }
}