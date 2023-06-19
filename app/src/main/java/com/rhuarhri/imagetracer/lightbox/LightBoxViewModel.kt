package com.rhuarhri.imagetracer.lightbox

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rhuarhri.imagetracer.cameratrace.EditImageSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LightBoxViewModel @Inject constructor(
    private val repository : LightBoxRepository
) : ViewModel() {

    private var originalBitmap : Bitmap? = null
    private var imageEditingSettings = EditImageSettings()

    private var _tracingImage : MutableStateFlow<Bitmap?> = MutableStateFlow(null)

    fun getImage() = viewModelScope.launch {
        val image = repository.getImage()

        withContext(Dispatchers.Main) {
            originalBitmap = image
            _tracingImage.update {
                image
            }
        }
    }

    private fun editImage() {
        originalBitmap?.let {
            viewModelScope.launch {
                val edited = repository.editImage(it, imageEditingSettings)

                withContext(Dispatchers.Main) {
                    _tracingImage.update {
                        edited
                    }
                }
            }
        }
    }

    fun setTransparency(transparency : Int) {
        imageEditingSettings.transparency = transparency
        editImage()
    }
}