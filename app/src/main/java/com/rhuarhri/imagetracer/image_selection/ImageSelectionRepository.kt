package com.rhuarhri.imagetracer.image_selection

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.annotation.WorkerThread
import com.rhuarhri.imagetracer.database.ImageDao
import com.rhuarhri.imagetracer.database.ImageEntity
import com.rhuarhri.imagetracer.utils.ImageUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File
import java.net.URL
import java.util.Date
import javax.inject.Inject
import kotlin.jvm.Throws

class ImageSelectionRepository @Inject constructor(
    private val dao : ImageDao
    ) : ImageSelectionRepositoryInterface {

    @WorkerThread
    override suspend fun saveImage(context : Context, image : Bitmap) {

        val uri = ImageUtils.bitmapToFile(context, image).toURI()

        dao.addImage(ImageEntity(
            created = Date().time,
            data = uri.path
        ))
    }

    @WorkerThread
    override suspend fun delete() {
        val unnecessaryImages = dao.unnecessaryImages()
        unnecessaryImages.forEach {
            val path = it.data

            try {
                File(path).delete()
            } catch (e : Exception) {
                //file might not exist
            }

            dao.deleteImage(it)
        }
    }

    override fun getImageFromUri(context: Context, uri: Uri): Bitmap? {
        val stream = context.contentResolver.openInputStream(uri)
        return BitmapFactory.decodeStream(stream)
    }

    override suspend fun getImageFromUrl(url: String): Bitmap? {

        return try {
            val inputStream = withContext(Dispatchers.IO) {
                URL(url).openStream()
            }
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            throw (e)
        }
    }

    override fun getHistory(): Flow<List<String>> {
        /*All the images are stored as byte arrays. As a result they are converted into bitmaps
        here.
         */
        return dao.getImageHistory().map { entities ->
            entities.mapNotNull { image ->
                if (File(image.data).exists()) {
                    image.data
                } else {
                    dao.deleteImage(image)
                    null
                }
            }
        }
    }
}

interface ImageSelectionRepositoryInterface {

    @WorkerThread
    suspend fun saveImage(context : Context, image : Bitmap)

    @WorkerThread
    suspend fun delete()

    fun getImageFromUri(context : Context, uri : Uri) : Bitmap?

    @Throws(Exception::class)
    suspend fun getImageFromUrl(url: String) : Bitmap?

    fun getHistory() : Flow<List<String>>
}