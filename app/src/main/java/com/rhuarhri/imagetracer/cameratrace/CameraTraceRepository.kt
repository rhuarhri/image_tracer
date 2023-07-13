package com.rhuarhri.imagetracer.cameratrace

import android.graphics.Bitmap
import com.rhuarhri.imagetracer.database.EditingSettingsDao
import com.rhuarhri.imagetracer.database.ImageDao
import com.rhuarhri.imagetracer.editing.Blur
import com.rhuarhri.imagetracer.editing.ColourMerging
import com.rhuarhri.imagetracer.editing.ColourOverlay
import com.rhuarhri.imagetracer.editing.EdgeDetection
import com.rhuarhri.imagetracer.editing.Flip
import com.rhuarhri.imagetracer.features.EditingSettings
import com.rhuarhri.imagetracer.utils.ImageUtils
import javax.inject.Inject


class CameraTraceRepository @Inject constructor(
    private val imageDao : ImageDao,
    private val editImageSettingsDao: EditingSettingsDao
) : CameraTraceRepositoryInterface {

    override suspend fun getImage(): Bitmap? {

        val imageData = imageDao.getImage().first()

        //update settings if there are no settings fro this image
        editImageSettingsDao.checkAndUpdateSettings(imageData.id)

        val bitmap = ImageUtils.getImageFromFile(imageData.data)

        if (bitmap == null) {
            /*
            If the bitmap is null then the either does not exist or the file is not an
            image. So the file and teh image entity should be deleted.
             */

            imageDao.deleteImage(imageData)
        }

        return bitmap
    }

    override suspend fun getEditSettings(): EditingSettings {
        return editImageSettingsDao.getSettings().map { setting ->
            EditingSettings(
                setting.blackAndWhite,
                Blur.Companion.Level.values()[setting.blur],
                ColourMerging.Companion.Level.values()[setting.colourMerging],
                ColourOverlay.Companion.Type.values()[setting.colourOverlay],
                setting.red,
                setting.green,
                setting.blue,
                setting.contrast,
                setting.brightness,
                EdgeDetection.Companion.Level.values()[setting.edgeDetection],
                Flip.Companion.Type.values()[setting.flip],
                setting.imageSegmentation,
                setting.invert,
                setting.isMonochrome,
                setting.transparency
            )
        }.firstOrNull() ?: EditingSettings()
    }
}

interface CameraTraceRepositoryInterface {

    suspend fun getImage() : Bitmap?

    suspend fun getEditSettings() : EditingSettings
}
