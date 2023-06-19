package com.rhuarhri.imagetracer.cameratrace

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.rhuarhri.imagetracer.database.ImageDao
import com.rhuarhri.imagetracer.utils.ImageUtils
import javax.inject.Inject


class CameraTraceRepository @Inject constructor(private val imageDao : ImageDao) {

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


data class EditImageSettings(
    var transparency :  Int = 100, //as a percentage
    var red : Int = 100, //as a percentage
    var green : Int = 100, //as a percentage
    var blue : Int = 100, //as a percentage
    var rotation : Int = 0,//as 0 to 360
    var size : Int = 0,
    var isMonochrome : Boolean = false
)
