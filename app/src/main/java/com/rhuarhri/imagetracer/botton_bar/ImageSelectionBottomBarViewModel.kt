package com.rhuarhri.imagetracer.botton_bar

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ImageSelectionBottomBarViewModel : BottomBarViewModel() {

    val repository = ImageSelectionBottomBarRepository()
    private val _selectedBitmap : MutableStateFlow<Bitmap?> = MutableStateFlow(null)
    val selectedBitmap : StateFlow<Bitmap?> = _selectedBitmap

    private val _showInternetError : MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showInternetError : StateFlow<Boolean> = _showInternetError

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

    fun getFromInternet(url : String) {
        //val url = "https://cdn.mos.cms.futurecdn.net/BX7vjSt8KMtcBHyisvcSPK.jpg"
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getImageFromUrl(url)

            withContext(Dispatchers.Main) {
                val bitmap = result.first
                bitmap?.let {
                    _selectedBitmap.update {
                        bitmap
                    }
                }

                //the error
                result.second?.let {
                    _showInternetError.update {
                        true
                    }
                }
            }
        }
    }

    fun seenError() {
        _showInternetError.update {
            false
        }
    }

    fun getFromSample(context: Context, fileName: String) {
        val bitmap = repository.getImageFromAsset(context, fileName)
        bitmap?.let {
            _selectedBitmap.update {
                bitmap
            }
        }
    }
}