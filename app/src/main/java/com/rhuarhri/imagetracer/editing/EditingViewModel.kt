package com.rhuarhri.imagetracer.editing

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rhuarhri.imagetracer.features.DEFAULT_BLACK_AND_WHITE
import com.rhuarhri.imagetracer.features.DEFAULT_BLUR
import com.rhuarhri.imagetracer.features.DEFAULT_BRIGHTNESS
import com.rhuarhri.imagetracer.features.DEFAULT_BlUE
import com.rhuarhri.imagetracer.features.DEFAULT_COLOUR_MERGING
import com.rhuarhri.imagetracer.features.DEFAULT_COLOUR_OVERLAY
import com.rhuarhri.imagetracer.features.DEFAULT_CONTRAST
import com.rhuarhri.imagetracer.features.DEFAULT_EDGE_DETECTION
import com.rhuarhri.imagetracer.features.DEFAULT_FLIP
import com.rhuarhri.imagetracer.features.DEFAULT_GREEN
import com.rhuarhri.imagetracer.features.DEFAULT_IMAGE_SEGMENTATION
import com.rhuarhri.imagetracer.features.DEFAULT_INVERT
import com.rhuarhri.imagetracer.features.DEFAULT_MONOCHROME
import com.rhuarhri.imagetracer.features.DEFAULT_RED
import com.rhuarhri.imagetracer.features.DEFAULT_TRANSPARENCY
import com.rhuarhri.imagetracer.features.EditingSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class EditingViewModel constructor(
    private val repository : EditingRepositoryInterface
) : ViewModel() {

    private var original : Bitmap? = null
    private val _tracingBitmap : MutableStateFlow<Bitmap?> = MutableStateFlow(null)
    val tracingBitmap : StateFlow<Bitmap?> = _tracingBitmap

    private val _editSettings : MutableStateFlow<EditingSettings> = MutableStateFlow(
        EditingSettings()
    )

    fun setImage(image : Bitmap) {
        original = image
        _tracingBitmap.update {
            image
        }
    }

    fun setSettings(settings : EditingSettings) {
        setBlackAndWhite(settings.blackAndWhite)
        setBlur(settings.blur)
        setColourMerging(settings.colourMerging)
        setColourOverlay(settings.colourOverlay)
        setColourRemover(settings.red, settings.green, settings.blue)
        setContrast(settings.contrast, settings.brightness)
        setEdgeDetection(settings.edgeDetection)
        setFlip(settings.flip)
        setImageSegmentation(settings.imageSegmentation)
        setInvert(settings.invert)
        setMonochrome(settings.isMonochrome)
        setTransparency(settings.transparency)

        _editSettings.update {
            settings
        }

        edit()
    }

    fun resetEditing() {
        resetBlackAndWhite()
        resetBlur()
        resetColourMerging()
        resetColourOverlay()
        resetColourRemover()
        resetContrast()
        resetEdgeDetection()
        resetFlip()
        resetImageSegmentation()
        resetInvert()
        resetMonochrome()
        resetTransparency()

        _editSettings.update {
            EditingSettings()
        }

        //save settings
        viewModelScope.launch {
            repository.setEditSettings(EditingSettings())
        }

        _tracingBitmap.update {
            original
        }
    }

    fun edit() = viewModelScope.launch {
        original?.let { image ->

            val settings = _editSettings.value

            val edited = repository.edit(image, settings)

            //save settings
            repository.setEditSettings(settings)

            withContext(Dispatchers.Main) {
                _tracingBitmap.update {
                    edited
                }
            }
        }
    }

    private val _blackAndWhite : MutableStateFlow<Boolean> = MutableStateFlow(DEFAULT_BLACK_AND_WHITE)
    val blackAndWhite : StateFlow<Boolean> = _blackAndWhite

    fun setBlackAndWhite(isBlackAndWhite: Boolean) {
        _blackAndWhite.update {
            isBlackAndWhite
        }

        _editSettings.update {
            it.copy(
                blackAndWhite = isBlackAndWhite
            )
        }
    }

    fun resetBlackAndWhite() = setBlackAndWhite(DEFAULT_BLACK_AND_WHITE)

    private val _blur : MutableStateFlow<Blur.Companion.Level> = MutableStateFlow(DEFAULT_BLUR)
    val blur : StateFlow<Blur.Companion.Level> = _blur

    fun setBlur(blur : Blur.Companion.Level) {
        _blur.update {
            blur
        }
        _editSettings.update {
            it.copy(
                blur = blur
            )
        }
    }

    fun resetBlur() = setBlur(DEFAULT_BLUR)

    private val _colourMerging : MutableStateFlow<ColourMerging.Companion.Level> =
        MutableStateFlow(DEFAULT_COLOUR_MERGING)
    val colourMerging : StateFlow<ColourMerging.Companion.Level> = _colourMerging

    fun setColourMerging(colourMerging : ColourMerging.Companion.Level) {
        _colourMerging.update {
            colourMerging
        }
        _editSettings.update {
            it.copy(
                colourMerging = colourMerging
            )
        }
    }

    fun resetColourMerging() = setColourMerging(DEFAULT_COLOUR_MERGING)

    private val _colourOverlay : MutableStateFlow<ColourOverlay.Companion.Type> =
        MutableStateFlow(DEFAULT_COLOUR_OVERLAY)
    val colourOverlay : StateFlow<ColourOverlay.Companion.Type> = _colourOverlay

    fun setColourOverlay(colourOverlay : ColourOverlay.Companion.Type) {
        _colourOverlay.update {
            colourOverlay
        }
        _editSettings.update {
            it.copy(
                colourOverlay = colourOverlay
            )
        }
    }

    fun resetColourOverlay() = setColourOverlay(DEFAULT_COLOUR_OVERLAY)

    private val _red : MutableStateFlow<Int> = MutableStateFlow(DEFAULT_RED)
    val red : StateFlow<Int> = _red

    private val _green : MutableStateFlow<Int> = MutableStateFlow(DEFAULT_GREEN)
    val green : StateFlow<Int> = _green

    private val _blue : MutableStateFlow<Int> = MutableStateFlow(DEFAULT_BlUE)
    val blue : StateFlow<Int> = _blue

    fun setColourRemover(red : Int, green : Int, blue : Int) {
        _red.update {
            red
        }

        _green.update {
            green
        }

        _blue.update {
            blue
        }
        _editSettings.update {
            it.copy(
                red = red,
                green = green,
                blue = blue
            )
        }
    }

    fun resetColourRemover() = setColourRemover(DEFAULT_RED, DEFAULT_GREEN, DEFAULT_BlUE)

    private val _contrast : MutableStateFlow<Int> = MutableStateFlow(DEFAULT_CONTRAST)
    val contrast : StateFlow<Int> = _contrast

    private val _brightness : MutableStateFlow<Int> = MutableStateFlow(DEFAULT_BRIGHTNESS)
    val brightness : StateFlow<Int> = _brightness

    fun setContrast(contrast : Int, brightness : Int) {
        _contrast.update {
            contrast
        }

        _brightness.update {
            brightness
        }

        _editSettings.update {
            it.copy(
                contrast = contrast,
                brightness = brightness
            )
        }
    }

    fun resetContrast() = setContrast(DEFAULT_CONTRAST, DEFAULT_BRIGHTNESS)

    private val _edgeDetection : MutableStateFlow<EdgeDetection.Companion.Level> =
        MutableStateFlow(DEFAULT_EDGE_DETECTION)
    val edgeDetection : StateFlow<EdgeDetection.Companion.Level> = _edgeDetection

    fun setEdgeDetection(edgeDetection: EdgeDetection.Companion.Level) {
        _edgeDetection.update {
            edgeDetection
        }
        _editSettings.update {
            it.copy(
                edgeDetection = edgeDetection
            )
        }
    }

    fun resetEdgeDetection() = setEdgeDetection(DEFAULT_EDGE_DETECTION)

    private val _flip : MutableStateFlow<Flip.Companion.Type> =
        MutableStateFlow(DEFAULT_FLIP)
    val flip : StateFlow<Flip.Companion.Type> = _flip

    fun setFlip(flip : Flip.Companion.Type) {
        _flip.update {
            flip
        }
        _editSettings.update {
            it.copy(
                flip = flip
            )
        }
    }

    fun resetFlip() = setFlip(DEFAULT_FLIP)

    private val _imageSegmentation : MutableStateFlow<Boolean> =
        MutableStateFlow(DEFAULT_IMAGE_SEGMENTATION)
    val imageSegmentation : StateFlow<Boolean> = _imageSegmentation

    fun setImageSegmentation(imageSegmentation: Boolean) {
        _imageSegmentation.update {
            imageSegmentation
        }
        _editSettings.update {
            it.copy(
                imageSegmentation = imageSegmentation
            )
        }
    }

    fun resetImageSegmentation() = setImageSegmentation(DEFAULT_IMAGE_SEGMENTATION)

    private val _invert : MutableStateFlow<Boolean> =
        MutableStateFlow(DEFAULT_INVERT)
    val invert : StateFlow<Boolean> = _invert

    fun setInvert(invert : Boolean) {
        _invert.update {
            invert
        }
        _editSettings.update {
            it.copy(
                invert = invert
            )
        }
    }

    fun resetInvert() = setInvert(DEFAULT_INVERT)

    private val _monochrome : MutableStateFlow<Boolean> =
        MutableStateFlow(DEFAULT_MONOCHROME)
    val monochrome : StateFlow<Boolean> = _monochrome

    fun setMonochrome(monochrome: Boolean) {
        _monochrome.update {
            monochrome
        }
        _editSettings.update {
            it.copy(
                isMonochrome = monochrome
            )
        }
    }

    fun resetMonochrome() = setMonochrome(DEFAULT_MONOCHROME)

    private val _transparency : MutableStateFlow<Int> =
        MutableStateFlow(DEFAULT_TRANSPARENCY)
    val transparency : StateFlow<Int> = _transparency

    fun setTransparency(transparency: Int) {
        _transparency.update {
            transparency
        }
        _editSettings.update {
            it.copy(
                transparency = transparency
            )
        }
    }

    fun resetTransparency() = setTransparency(DEFAULT_TRANSPARENCY)
}