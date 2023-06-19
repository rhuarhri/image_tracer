package com.rhuarhri.imagetracer.botton_bar

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.rhuarhri.imagetracer.utils.ImageUtils
import java.lang.Exception
import java.net.URL

class ImageSelectionBottomBarRepository {

    fun getImageFromUri(context : Context, uri : Uri) : Bitmap? {
        val stream = context.contentResolver.openInputStream(uri)
        return BitmapFactory.decodeStream(stream)
    }

    suspend fun getImageFromUrl(url: String): Pair<Bitmap?, String?> {
        return try {
            val inputStream = URL(url).openStream()
            Pair(
                first = BitmapFactory.decodeStream(inputStream),
                second = null
            )
        } catch (e: Exception) {
            Pair(
                first = null,
                second = e.toString()
            )
        }
    }

    fun getImageFromAsset(context: Context, fileName: String): Bitmap? {
        return ImageUtils.assetFileToBitmap(context, fileName)
    }

}