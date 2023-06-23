package com.rhuarhri.imagetracer.image_selection

import androidx.annotation.WorkerThread
import com.rhuarhri.imagetracer.database.ImageDao
import com.rhuarhri.imagetracer.database.ImageEntity
import java.util.Date
import javax.inject.Inject

class ImageSelectionRepository @Inject constructor(
    private val dao : ImageDao
    ) : ImageSelectionRepositoryInterface {

    @WorkerThread
    override suspend fun saveImage(image : ByteArray) {
        dao.addImage(ImageEntity(
            created = Date().time,
            data = image
        ))
    }

    @WorkerThread
    override suspend fun delete() {
        dao.deleteUnnecessaryImages()
    }
}

interface ImageSelectionRepositoryInterface {

    @WorkerThread
    suspend fun saveImage(image : ByteArray)

    @WorkerThread
    suspend fun delete()
}