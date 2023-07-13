package com.rhuarhri.imagetracer.image_selection

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ImageSelectionViewModel @Inject constructor(
    private val repository: ImageSelectionRepositoryInterface
) : ViewModel() {

    private val _selectedBitmap : MutableStateFlow<Bitmap?> = MutableStateFlow(null)
    val selectedBitmap : StateFlow<Bitmap?> = _selectedBitmap

    fun saveImage(context : Context) = viewModelScope.launch(Dispatchers.IO) {
        _selectedBitmap.value?.let { image ->
            //save the new image
            repository.saveImage(context, image)

            //check for and delete any unnecessary images
            repository.delete()
        }
    }

    private val _showInternetError : MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showInternetError : StateFlow<Boolean> = _showInternetError

    fun hideInternetError() {
        _showInternetError.update {
            false
        }
    }

    fun getFromStorage(context: Context, uri: Uri) {
        val bitmap = repository.getImageFromUri(context, uri)
        bitmap?.let {
            _selectedBitmap.update {
                bitmap
            }
        }
    }

    fun getFromCamera(context: Context, uri: Uri) {
        val bitmap = repository.getImageFromUri(context, uri)
        bitmap?.let {
            _selectedBitmap.update {
                bitmap
            }
        }
    }

    fun getFromInternet(url : String) =
        //examole url = "https://cdn.mos.cms.futurecdn.net/BX7vjSt8KMtcBHyisvcSPK.jpg"
        viewModelScope.launch(Dispatchers.IO) {
            var error: String? = null
            val bitmap = try {
                repository.getImageFromUrl(url)
            } catch (e: Exception) {
                error = e.message
                null
            }

            withContext(Dispatchers.Main) {
                bitmap?.let {
                    _selectedBitmap.update {
                        bitmap
                    }
                }

                //the error
                error?.let {
                    _showInternetError.update {
                        true
                    }
                }
            }
        }

    var history: StateFlow<List<String>> = repository.getHistory().stateIn(
        initialValue = emptyList(),
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000)
    )

    fun setSelectedBitmap(bitmap: Bitmap) {
        _selectedBitmap.update {
            bitmap
        }
    }
}