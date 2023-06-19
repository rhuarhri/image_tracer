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
    private val repository: ImageTracingBottomBarRepository)
    : BottomBarViewModel() {

    private var original : Bitmap? = null
    private val _tracingBitmap : MutableStateFlow<Bitmap?> = MutableStateFlow(null)
    val tracingBitmap : StateFlow<Bitmap?> = _tracingBitmap

    //moving the image
    private val DEFAULT_SCALE = 1f
    private val _scale : MutableStateFlow<Float> = MutableStateFlow(DEFAULT_SCALE)
    val scale : StateFlow<Float> = _scale

    private val DEFAULT_ROTATION = 0f
    private val _rotation : MutableStateFlow<Float> = MutableStateFlow(DEFAULT_ROTATION)
    val rotation : StateFlow<Float> = _rotation

    private val DEFAULT_OFFSET = Offset.Zero
    private val _offset : MutableStateFlow<Offset> = MutableStateFlow(DEFAULT_OFFSET)
    val offset : StateFlow<Offset> = _offset

    private val DEFAULT_ENDABLE_PINCH_TO_ZOOM = false
    private val _enablePinchToZoom : MutableStateFlow<Boolean> =
        MutableStateFlow(DEFAULT_ENDABLE_PINCH_TO_ZOOM)
    val enablePinchToZoom : StateFlow<Boolean> = _enablePinchToZoom

    private val DEFAULT_INVERT = false
    private val _invert : MutableStateFlow<Boolean> = MutableStateFlow(DEFAULT_INVERT)
    val invert : StateFlow<Boolean> = _invert

    private val DEFAULT_RED = 100
    private val _red : MutableStateFlow<Int> = MutableStateFlow(DEFAULT_RED)
    val red : StateFlow<Int> = _red

    private val DEFAULT_GREEN = 100
    private val _green : MutableStateFlow<Int> = MutableStateFlow(DEFAULT_GREEN)
    val green : StateFlow<Int> = _green

    private val DEFAULT_BlUE = 100
    private val _blue : MutableStateFlow<Int> = MutableStateFlow(DEFAULT_BlUE)
    val blue : StateFlow<Int> = _blue

    private val DEFAULT_TRANSPARENCY = 100
    private val _transparency : MutableStateFlow<Int> = MutableStateFlow(DEFAULT_TRANSPARENCY)
    val transparency : StateFlow<Int> = _transparency

    private val DEFAULT_CONTRAST = 10
    private val _contrast : MutableStateFlow<Int> = MutableStateFlow(DEFAULT_CONTRAST)
    val contrast : StateFlow<Int> = _contrast

    private val DEFAULT_BRIGHTNESS = 0
    private val _brightness : MutableStateFlow<Int> = MutableStateFlow(DEFAULT_BRIGHTNESS)
    val brightness : StateFlow<Int> = _brightness

    private val DEFAULT_LUMINANCE = 0
    private val _luminance : MutableStateFlow<Int> = MutableStateFlow(DEFAULT_LUMINANCE)
    val luminance : StateFlow<Int> = _luminance

    private val DEFAULT_MONOCHROME = false
    private val _isMonochrome : MutableStateFlow<Boolean> = MutableStateFlow(DEFAULT_MONOCHROME)
    val isMonochrome : StateFlow<Boolean> = _isMonochrome

    fun getImage() = viewModelScope.launch {
        val image = repository.getImage()

        withContext(Dispatchers.Main) {
            original = image
            _tracingBitmap.update {
                image
            }
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
        setOffset(DEFAULT_OFFSET)
    }

    fun setEnablePinchToZoom(enablePinchToZoom : Boolean) {
        _enablePinchToZoom.update {
            enablePinchToZoom
        }
    }

    fun resetEnablePinchToZoom() {
        setEnablePinchToZoom(DEFAULT_ENDABLE_PINCH_TO_ZOOM)
    }

    fun setInvert(invert : Boolean) {
        _invert.update {
            invert
        }
    }

    fun resetInvert() {
        setInvert(DEFAULT_INVERT)
    }

    fun setRed(red : Int) {
        _red.update {
            red
        }
    }

    fun resetRed() {
        setRed(DEFAULT_RED)
    }

    fun setGreen(green : Int) {
        _green.update {
            green
        }
    }

    fun resetGreen() {
        setGreen(DEFAULT_GREEN)
    }

    fun setBlue(blue : Int) {
        _blue.update {
            blue
        }
    }

    fun resetBlue() {
        setBlue(DEFAULT_BlUE)
    }

    fun setTransparency(transparency : Int) {
        _transparency.update {
            transparency
        }
    }

    fun resetTransparency() {
        setTransparency(DEFAULT_TRANSPARENCY)
    }

    fun setContrast(contrast: Int) {
        _contrast.update {
            contrast
        }
    }

    fun resetContrast() {
        setContrast(DEFAULT_CONTRAST)
    }

    fun setBrightness(brightness : Int) {
        _brightness.update {
            brightness
        }
    }

    fun resetBrightness() {
        setBrightness(DEFAULT_BRIGHTNESS)
    }

    fun setLuminance(luminance : Int) {
        _luminance.update {
            luminance
        }
    }

    fun resetLuminance() {
        setLuminance(DEFAULT_LUMINANCE)
    }

    fun setMonochrome(isMonochrome : Boolean) {
        _isMonochrome.update {
            isMonochrome
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
                val settings = EditImageSettings(
                    _transparency.value,
                    _invert.value,
                    _red.value,
                    _green.value,
                    _blue.value,
                    _contrast.value,
                    _brightness.value,
                    _luminance.value,
                    _isMonochrome.value
                )

                val edited = repository.editImage(it, settings)

                withContext(Dispatchers.Main) {
                    _tracingBitmap.update {
                        edited
                    }
                }
            }
        }
    }

}