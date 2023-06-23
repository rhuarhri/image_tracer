package com.rhuarhri.imagetracer.botton_bar

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ImageTracingBottomBarViewModel @Inject constructor(
    private val repository: ImageTracingBottomBarRepositoryInterface)
    : BottomBarViewModel() {

    private var original : Bitmap? = null
    private val _tracingBitmap : MutableStateFlow<Bitmap?> = MutableStateFlow(null)
    val tracingBitmap : StateFlow<Bitmap?> = _tracingBitmap

    private val _editSettings : MutableStateFlow<EditImageSettings> = MutableStateFlow(
        EditImageSettings()
    )
    val editSettings : StateFlow<EditImageSettings> = _editSettings

    //moving the image
    private val _scale : MutableStateFlow<Float> = MutableStateFlow(DEFAULT_SCALE)
    val scale : StateFlow<Float> = _scale

    private val _rotation : MutableStateFlow<Float> = MutableStateFlow(DEFAULT_ROTATION)
    val rotation : StateFlow<Float> = _rotation

    private val _offset : MutableStateFlow<Offset> = MutableStateFlow(Offset(DEFAULT_OFFSET_X, DEFAULT_OFFSET_Y))
    val offset : StateFlow<Offset> = _offset

    private val _enablePinchToZoom : MutableStateFlow<Boolean> =
        MutableStateFlow(DEFAULT_ENABLE_PINCH_TO_ZOOM)
    val enablePinchToZoom : StateFlow<Boolean> = _enablePinchToZoom

    init {
        setUpImage()
    }

    private fun setUpImage() = viewModelScope.launch(Dispatchers.IO) {
        val image = repository.getImage()

        val settings = repository.getSettings()

        withContext(Dispatchers.Main) {
            original = image
            _tracingBitmap.update {
                image
            }
            _editSettings.update {
                settings
            }
            edit()
        }
    }

    fun setScale(scale : Float) {
        _scale.update {
            scale
        }
    }

    fun resetScale() {
        setScale(DEFAULT_SCALE)
    }

    fun setRotation(rotation : Float) {
        _rotation.update {
            rotation
        }
    }

    fun resetRotation() {
        setRotation(DEFAULT_ROTATION)
    }

    fun setOffset(offset: Offset) {
        _offset.update {
            offset
        }
    }

    fun resetOffset() {
        setOffset(Offset(DEFAULT_OFFSET_X, DEFAULT_OFFSET_Y))
    }

    fun setEnablePinchToZoom(enablePinchToZoom : Boolean) {
        _enablePinchToZoom.update {
            enablePinchToZoom
        }
    }

    fun resetEnablePinchToZoom() {
        setEnablePinchToZoom(DEFAULT_ENABLE_PINCH_TO_ZOOM)
    }

    fun setInvert(invert : Boolean) {
        _editSettings.update {
            it.copy(invert = invert)
        }
    }

    fun resetInvert() {
        setInvert(DEFAULT_INVERT)
    }

    fun setRed(red : Int) {
        _editSettings.update {
            it.copy(red = red)
        }
    }

    fun resetRed() {
        setRed(DEFAULT_RED)
    }

    fun setGreen(green : Int) {
        _editSettings.update {
            it.copy(green = green)
        }
    }

    fun resetGreen() {
        setGreen(DEFAULT_GREEN)
    }

    fun setBlue(blue : Int) {
        _editSettings.update {
            it.copy(blue = blue)
        }
    }

    fun resetBlue() {
        setBlue(DEFAULT_BlUE)
    }

    fun setTransparency(transparency : Int) {
        _editSettings.update {
            it.copy(transparency = transparency)
        }
    }

    fun resetTransparency() {
        setTransparency(DEFAULT_TRANSPARENCY)
    }

    fun setContrast(contrast: Int) {
        _editSettings.update {
            it.copy(contrast = contrast)
        }
    }

    fun resetContrast() {
        setContrast(DEFAULT_CONTRAST)
    }

    fun setBrightness(brightness : Int) {
        _editSettings.update {
            it.copy(brightness = brightness)
        }
    }

    fun resetBrightness() {
        setBrightness(DEFAULT_BRIGHTNESS)
    }

    fun setLuminance(luminance : Int) {
        _editSettings.update {
            it.copy(luminance = luminance)
        }
    }

    fun resetLuminance() {
        setLuminance(DEFAULT_LUMINANCE)
    }

    fun setMonochrome(isMonochrome : Boolean) {
        _editSettings.update {
            it.copy(isMonochrome = isMonochrome)
        }
    }

    fun resetMonochrome() {
        setMonochrome(DEFAULT_MONOCHROME)
    }

    fun resetAll() {
        resetScale()
        resetRotation()
        resetOffset()
        resetEnablePinchToZoom()
        resetInvert()
        resetRed()
        resetGreen()
        resetBlue()
        resetTransparency()
        resetContrast()
        resetBrightness()
        resetLuminance()
        resetMonochrome()

        edit()
    }

    fun edit() {
        original?.let {
            viewModelScope.launch(Dispatchers.Default) {
                val edited = repository.editImage(it, _editSettings.value)

                withContext(Dispatchers.Main) {
                    _tracingBitmap.update {
                        edited
                    }
                }
            }
        }
    }

}