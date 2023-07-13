package com.rhuarhri.imagetracer.lightbox

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.viewModelScope
import com.rhuarhri.imagetracer.editing.EditingRepositoryInterface
import com.rhuarhri.imagetracer.editing.EditingViewModel
import com.rhuarhri.imagetracer.features.DEFAULT_ENABLE_PINCH_TO_ZOOM
import com.rhuarhri.imagetracer.features.DEFAULT_OFFSET_X
import com.rhuarhri.imagetracer.features.DEFAULT_OFFSET_Y
import com.rhuarhri.imagetracer.features.DEFAULT_ROTATION
import com.rhuarhri.imagetracer.features.DEFAULT_SCALE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LightBoxViewModel @Inject constructor(
    private val repository: LightBoxRepository,
    editingRepository: EditingRepositoryInterface
) : EditingViewModel(editingRepository) {

    private val _darkMode : MutableStateFlow<Boolean> = MutableStateFlow(false)
    val darkMode : StateFlow<Boolean> = _darkMode
    fun setDarkMode(isDarkMode : Boolean) {
        _darkMode.update {
            isDarkMode
        }
    }

    init {
        setup()
    }

    private fun setup() = viewModelScope.launch(Dispatchers.IO) {
        val image = repository.getImage()
        val settings = repository.getEditSettings()

        withContext(Dispatchers.Main) {
            image?.let {
                setImage(it)
                setSettings(settings)
            }
        }
    }

    private val _scale : MutableStateFlow<Float> = MutableStateFlow(DEFAULT_SCALE)
    val scale : StateFlow<Float> = _scale

    fun setScale(scale : Float) {
        _scale.update {
            scale
        }
    }

    fun resetScale() {
        setScale(DEFAULT_SCALE)
    }

    private val _rotation : MutableStateFlow<Float> = MutableStateFlow(DEFAULT_ROTATION)
    val rotation : StateFlow<Float> = _rotation

    fun setRotation(rotation : Float) {
        _rotation.update {
            rotation
        }
    }

    fun resetRotation() {
        setRotation(DEFAULT_ROTATION)
    }

    private val _offset : MutableStateFlow<Offset> = MutableStateFlow(Offset(DEFAULT_OFFSET_X, DEFAULT_OFFSET_Y))
    val offset : StateFlow<Offset> = _offset

    fun setOffset(offset: Offset) {
        _offset.update {
            offset
        }
    }

    fun resetOffset() {
        setOffset(Offset(DEFAULT_OFFSET_X, DEFAULT_OFFSET_Y))
    }

    private val _enablePinchToZoom : MutableStateFlow<Boolean> =
        MutableStateFlow(DEFAULT_ENABLE_PINCH_TO_ZOOM)
    val enablePinchToZoom : StateFlow<Boolean> = _enablePinchToZoom

    fun setEnablePinchToZoom(enablePinchToZoom : Boolean) {
        _enablePinchToZoom.update {
            enablePinchToZoom
        }
    }

    fun resetEnablePinchToZoom() {
        setEnablePinchToZoom(DEFAULT_ENABLE_PINCH_TO_ZOOM)
    }

    fun resetAll() {
        resetScale()
        resetOffset()
        resetRotation()
        resetEnablePinchToZoom()
        resetEditing()
    }

}