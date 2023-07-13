package com.rhuarhri.imagetracer.editing

import android.graphics.Bitmap
import com.rhuarhri.imagetracer.database.EditingSettingsDao
import com.rhuarhri.imagetracer.features.EditingSettings

class EditingRepository(
    private val editImageSettingsDao: EditingSettingsDao
) : EditingRepositoryInterface {
    override suspend fun edit(image : Bitmap, settings : EditingSettings) : Bitmap {

        var edited = image

        edited = Flip().set(edited, settings.flip)

        //this is here because if the chooses either edge detection and image
        //segmentation all other edit effect won't be visible. For example if the user
        //selects edge detection then they won't see ant changes to contrast
        //this also helps to improve performance by excluding unnecessary editing
        if (settings.edgeDetection != EdgeDetection.Companion.Level.NONE) {
            edited = EdgeDetection().set(edited, settings.edgeDetection)
            edited = Transparency().set(settings.transparency, edited)
            return edited
        }

        if (settings.imageSegmentation) {
            edited = ImageSegmentation().set(edited, true)
            edited = Transparency().set(settings.transparency, edited)
            return edited
        }

        //these effect colour so will have an effect on colour merging.
        //so need to go before colour merging.
        edited = Invert().set(edited, settings.invert)
        edited = Monochrome().set(edited, settings.isMonochrome)
        edited = BlackAndWhite().set(edited, settings.blackAndWhite)
        edited = Contrast().setPercentage(edited, settings.contrast, settings.brightness)

        //these will effect edge detection. so need to go before edge detection
        edited = ColourMerging().set(edited, settings.colourMerging)

        //this need to go after any editing related to colours.
        // That a if a colour is removed it won't added back in a future edit.
        edited = ColourRemover().set(settings.red, settings.green, settings.blue, edited)

        edited = Blur().set(edited, settings.blur)

        //These exist to make tracing the image easier to see.
        // So need to be at the end.
        edited = Transparency().set(settings.transparency, edited)
        edited = ColourOverlay().set(edited, settings.colourOverlay)

        return edited
    }

    override suspend fun setEditSettings(setting: EditingSettings) {
        editImageSettingsDao.updateSettings(
            setting.blackAndWhite,
            setting.blur.ordinal,
            setting.colourMerging.ordinal,
            setting.colourOverlay.ordinal,
            setting.red,
            setting.green,
            setting.blue,
            setting.contrast,
            setting.brightness,
            setting.edgeDetection.ordinal,
            setting.flip.ordinal,
            setting.imageSegmentation,
            setting.invert,
            setting.isMonochrome,
            setting.transparency
        )
    }
}

interface EditingRepositoryInterface {
    suspend fun edit(image : Bitmap, settings : EditingSettings) : Bitmap

    suspend fun setEditSettings(setting: EditingSettings)
}