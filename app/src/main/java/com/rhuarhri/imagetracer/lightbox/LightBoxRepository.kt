package com.rhuarhri.imagetracer.lightbox

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.rhuarhri.imagetracer.cameratrace.EditImageSettings
import com.rhuarhri.imagetracer.database.ImageDao
import com.rhuarhri.imagetracer.utils.ImageUtils
import javax.inject.Inject

class LightBoxRepository @Inject constructor(private val imageDao : ImageDao) {

    suspend fun getImage(): Bitmap {
        val imageData = imageDao.getImage().first()
        val imageBytes = imageData.data

        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    suspend fun editImage(origin : Bitmap, setting : EditImageSettings) : Bitmap {
        var editedBitmap = origin

        editedBitmap = ImageUtils.changeImageColour(setting.red, setting.green, setting.blue, editedBitmap)

        if (setting.isMonochrome) {
            editedBitmap = ImageUtils.setToMonochrome(editedBitmap)
        }

        editedBitmap = ImageUtils.setTransparency(setting.transparency, editedBitmap)

        editedBitmap = ImageUtils.setRotation(setting.rotation, editedBitmap)

        editedBitmap = ImageUtils.resizeImage(setting.size, editedBitmap)

        return editedBitmap
    }
}