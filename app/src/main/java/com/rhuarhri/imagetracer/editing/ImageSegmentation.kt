package com.rhuarhri.imagetracer.editing

import android.graphics.Bitmap
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.MatOfFloat
import org.opencv.core.MatOfInt
import org.opencv.core.Point
import org.opencv.core.Scalar
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc

class ImageSegmentation {

    fun set(image : Bitmap, enabled : Boolean) : Bitmap {
        if (!enabled) {
            return image
        }

        val imageMat = Mat()
        Utils.bitmapToMat(image, imageMat)

        Imgproc.cvtColor(imageMat, imageMat, Imgproc.COLOR_BGR2HSV)
        val imagePlanes = mutableListOf<Mat>()
        Core.split(imageMat, imagePlanes)

        // get the average hue value of the image
        val threshValue = getHistAverage(imageMat, imagePlanes[0])

        val thresholdImg = Mat()
        val max = 255.0//was 179.0
        Imgproc.threshold(imagePlanes[0], thresholdImg, threshValue, max, Imgproc.THRESH_BINARY_INV)

        Imgproc.blur(thresholdImg, thresholdImg, Size(5.0, 5.0))

        // dilate to fill gaps, erode to smooth edges
        Imgproc.dilate(thresholdImg, thresholdImg, Mat(), Point(-1.0, -1.0), 1)
        Imgproc.erode(thresholdImg, thresholdImg, Mat(), Point(-1.0, -1.0), 3)

        Imgproc.threshold(thresholdImg, thresholdImg, threshValue, 179.0, Imgproc.THRESH_BINARY)

        val foreground = Mat(
            Size(image.width.toDouble(), image.height.toDouble()),
            CvType.CV_8UC3,
            Scalar(255.0, 255.0, 255.0)
        )

        Imgproc.cvtColor(imageMat, imageMat, Imgproc.COLOR_HSV2BGR)
        imageMat.copyTo(foreground, thresholdImg)

        val bitmap = EditingUtils.createBlankBitmap(image.width, image.height)
        Utils.matToBitmap(foreground, bitmap)
        return bitmap
    }

    private fun getHistAverage(hsvImg: Mat, hueValues: Mat): Double {
        // init
        var average = 0.0
        val hist_hue = Mat()
        // 0-180: range of Hue values
        val histSize = MatOfInt(180)
        val hue: MutableList<Mat> = ArrayList()
        hue.add(hueValues)

        // compute the histogram
        Imgproc.calcHist(hue, MatOfInt(0), Mat(), hist_hue, histSize, MatOfFloat(0f, 179f))

        // get the average Hue value of the image
        // (sum(bin(h)*h))/(image-height*image-width)
        // -----------------
        // equivalent to get the hue of each pixel in the image, add them, and
        // divide for the image size (height and width)
        for (h in 0..179) {
            // for each bin, get its value and multiply it for the corresponding
            // hue
            average += hist_hue[h, 0][0] * h
        }

        // return the average hue of the image
        return average / hsvImg.size().height / hsvImg.size().width.also { average = it }
    }
}