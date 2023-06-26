package com.rhuarhri.imagetracer.image_selection

import android.content.Context
import android.graphics.Bitmap
import androidx.annotation.WorkerThread
import com.rhuarhri.imagetracer.database.ImageDao
import com.rhuarhri.imagetracer.database.ImageEntity
import com.rhuarhri.imagetracer.utils.ImageUtils
import java.io.File
import java.util.Date
import javax.inject.Inject

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
}

interface ImageSelectionRepositoryInterface {

    @WorkerThread
    suspend fun saveImage(context : Context, image : Bitmap)

    @WorkerThread
    suspend fun delete()
}