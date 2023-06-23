package com.rhuarhri.imagetracer.botton_bar

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
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
class ImageSelectionBottomBarViewModel @Inject constructor(
    private val repository : ImageSelectionBottomBarRepositoryInterface
    ) : BottomBarViewModel() {

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
        //examole url = "https://cdn.mos.cms.futurecdn.net/BX7vjSt8KMtcBHyisvcSPK.jpg"
        viewModelScope.launch(Dispatchers.IO) {
            var error : String? = null
            val bitmap = try {
                repository.getImageFromUrl(url)
            } catch (e : Exception) {
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
    }

    fun seenError() {
        _showInternetError.update {
            false
        }
    }

    val imageHistory : StateFlow<List<Bitmap>> = repository.getHistory().stateIn(
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