package com.rhuarhri.imagetracer.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

object ImageUtils {

    fun assetFileToBitmap(context: Context, fileName : String) : Bitmap? {
        return try {
            val stream = context.assets.open(fileName)

            BitmapFactory.decodeStream(stream)
        } catch (e : Exception) {
            null
        }
    }
}