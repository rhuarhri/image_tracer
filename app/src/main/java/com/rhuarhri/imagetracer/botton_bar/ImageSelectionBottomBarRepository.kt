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
import kotlin.jvm.Throws

class ImageSelectionBottomBarRepository @Inject constructor(
    private val imageDao : ImageDao
    ) : ImageSelectionBottomBarRepositoryInterface {

    override fun getImageFromUri(context : Context, uri : Uri) : Bitmap? {
        val stream = context.contentResolver.openInputStream(uri)
        return BitmapFactory.decodeStream(stream)
    }

    @Throws(Exception::class)
    override suspend fun getImageFromUrl(url: String): Bitmap? {
        /*this returns a pair. the first item is a image the second item is error*/

        return try {
            val inputStream = withContext(Dispatchers.IO) {
                URL(url).openStream()
            }
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            throw (e)
        }
    }

    override fun getHistory() : Flow<List<Bitmap>> {
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

interface ImageSelectionBottomBarRepositoryInterface {

    fun getImageFromUri(context : Context, uri : Uri) : Bitmap?

    @Throws(Exception::class)
    suspend fun getImageFromUrl(url: String) : Bitmap?

    fun getHistory() : Flow<List<Bitmap>>
}