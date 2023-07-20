package com.rhuarhri.imagetracer.editing

import android.graphics.Bitmap
import com.rhuarhri.imagetracer.features.DEFAULT_BlUE
import com.rhuarhri.imagetracer.features.DEFAULT_GREEN
import com.rhuarhri.imagetracer.features.DEFAULT_RED
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.Scalar
import org.opencv.imgproc.Imgproc

class ColourRemover {

    fun set(redPercentage : Int,
            greenPercentage : Int,
            bluePercentage : Int,
            image : Bitmap
    ) : Bitmap {

        if (redPercentage == DEFAULT_RED
            && greenPercentage == DEFAULT_GREEN
            && bluePercentage == DEFAULT_BlUE) {
            return image
        }

        val red = ((redPercentage * 255) / 100.0)
        val green = ((greenPercentage * 255) / 100.0)
        val blue = ((bluePercentage * 255) / 100.0)

        val imageMat = Mat()

        Utils.bitmapToMat(image, imageMat)
        Imgproc.cvtColor(imageMat, imageMat, Imgproc.COLOR_BGR2HSV)

        /*Colours             R         G         B*/
        val low = Scalar(0.0, 0.0, 0.0)
        val high = Scalar(red, green, blue)

        val mask = Mat()
        Core.inRange(imageMat, low, high, mask)

        val invMask = Mat()
        Core.bitwise_not(mask, invMask)

        val withSelectedColor = Mat()
        Core.bitwise_and(imageMat, imageMat, withSelectedColor, mask)

        val withoutSelectedColor = Mat()
        Core.bitwise_and(imageMat, imageMat, withoutSelectedColor, invMask)

        //when added together they must be the same type
        Imgproc.cvtColor(withSelectedColor, withSelectedColor, Imgproc.COLOR_HSV2BGR)

        //withoutSelectedColor is going to be in gray scale
        //step 1 convert from HSV as this type cannot be convert to gray scale
        Imgproc.cvtColor(withoutSelectedColor, withoutSelectedColor, Imgproc.COLOR_HSV2BGR)
        //step 2 convert tp gray scale
        Imgproc.cvtColor(withoutSelectedColor, withoutSelectedColor, Imgproc.COLOR_BGR2GRAY)
        //step 3 convert to the same type as withSelectedColor so it can be added together
        Imgproc.cvtColor(withoutSelectedColor, withoutSelectedColor, Imgproc.COLOR_GRAY2BGR)

        val edited = Mat()
        Core.add(withSelectedColor, withoutSelectedColor, edited)

        val bitmap = EditingUtils.createBlankBitmap(image.width, image.height)
        Utils.matToBitmap(edited, bitmap)
        return bitmap
    }
}