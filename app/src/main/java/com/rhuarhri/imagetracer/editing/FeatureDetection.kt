package com.rhuarhri.imagetracer.editing

import android.graphics.Bitmap
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.core.MatOfKeyPoint
import org.opencv.features2d.SIFT
import org.opencv.imgproc.Imgproc

class FeatureDetection {

    /**Waring this is still a working progress*/

    fun set(image : Bitmap) : Bitmap {

        val imageMat = Mat()
        val featureMat = Mat()

        Utils.bitmapToMat(image, imageMat)

        val grayMat = Mat()
        Imgproc.cvtColor(imageMat, grayMat, Imgproc.COLOR_BGR2GRAY)

        val featureDetector = SIFT.create()//ORB.create()//FastFeatureDetector.create()
        //featureDetector.nonmaxSuppression = false

        val keyPoints = MatOfKeyPoint()
        featureDetector.detect(grayMat, keyPoints)
        val mat = Mat()
        val description = Mat()
        featureDetector.detectAndCompute(grayMat, mat, keyPoints, description)

        /*keyPoints.toArray().forEach { point ->
            Imgproc.drawMarker(imageMat, point.pt, Scalar(0.0, 255.0, 0.0))
        }*/

        val bitmap = EditingUtils.createBlankBitmap(image.width, image.height)
        Utils.matToBitmap(mat, bitmap)

        return image
    }
}