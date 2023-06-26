package com.rhuarhri.imagetracer.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

object ImageUtils {

    fun assetFileToBitmap(context: Context, fileName : String) : Bitmap? {
        return try {
            val stream = context.assets.open(fileName)

            BitmapFactory.decodeStream(stream)
        } catch (e : Exception) {
            null
        }
    }

    fun bitmapToFile(context : Context, bitmap : Bitmap) : File {
        val id = UUID.randomUUID().toString()
        val file = File.createTempFile(id, ".png", context.cacheDir)

        val requiredHeight = 1000

        val requiredWidth = 563

        val smallImage = scaleBitmap(bitmap, requiredWidth, requiredHeight)

        val outStream = FileOutputStream(file)
        smallImage.compress(Bitmap.CompressFormat.PNG, 100, outStream)
        return file
    }

    fun getImageFromFile(filePath : String?) : Bitmap? {

        val bitmap = if (filePath != null) {
            BitmapFactory.decodeFile(filePath)

        } else {
            null
        }

        if (bitmap == null) {
            try {
                /*
            If the bitmap is null then the either does not exist or the file is not an
            image. So the file and teh image entity should be deleted.
             */
                filePath?.let {
                    File(it).delete()
                }
            } catch (e: Exception) {
                //file could not be deleted possibly because it does not exist
            }
        }

        return bitmap
    }

    fun getThumbNail(filePath : String?) : Bitmap? {
        val bitmap = getImageFromFile(filePath) ?: return null
        return scaleBitmap(bitmap, 200, 200)
    }

    private fun scaleBitmap(bitmap: Bitmap, requiredWidth: Int, requiredHeight: Int): Bitmap {

        var currentHeight = bitmap.height
        var currentWidth = bitmap.width
        while (currentWidth > requiredWidth && currentHeight > requiredHeight) {
            currentWidth /= 2
            currentHeight /= 2
        }

        /*
        The images are being scaled down here. This is because large images slow the app down
        without providing any benefit (because a large image and a small image look the same on a
         small smartphone screen).
         */
        return Bitmap.createScaledBitmap(bitmap, currentWidth, currentHeight, true)
    }
}