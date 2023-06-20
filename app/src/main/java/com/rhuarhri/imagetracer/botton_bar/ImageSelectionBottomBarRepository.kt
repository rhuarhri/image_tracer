package com.rhuarhri.imagetracer.botton_bar

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.rhuarhri.imagetracer.database.ImageDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.net.URL
import javax.inject.Inject

class ImageSelectionBottomBarRepository @Inject constructor(private val imageDao : ImageDao) {

    fun getImageFromUri(context : Context, uri : Uri) : Bitmap? {
        val stream = context.contentResolver.openInputStream(uri)
        return BitmapFactory.decodeStream(stream)
    }

    suspend fun getImageFromUrl(url: String): Pair<Bitmap?, String?> {
        /*this returns a pair. the first item is a image the second item is error*/

        return try {
            val inputStream = withContext(Dispatchers.IO) {
                URL(url).openStream()
            }
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

    fun getHistory() : Flow<List<Bitmap>> {
        /*All the images are stored as byte arrays. As a result they are converted into bitmaps
        here.
         */
        return imageDao.getImageHistory().map { entities ->
            entities.mapNotNull { image ->
                val imageBytes = image.data
                BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            }
        }
    }

}