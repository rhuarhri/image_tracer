package com.rhuarhri.imagetracer.image_selection

import androidx.annotation.WorkerThread
import com.rhuarhri.imagetracer.database.ImageDao
import com.rhuarhri.imagetracer.database.ImageEntity
import javax.inject.Inject

class ImageSelectionRepository @Inject constructor(private val dao : ImageDao) {

    @WorkerThread
    suspend fun saveImage(name : String, image : ByteArray) {
        val image = ImageEntity(
            name = name,
            //created = Date(),
            data = image
        )
        dao.addImage(image)
    }
}