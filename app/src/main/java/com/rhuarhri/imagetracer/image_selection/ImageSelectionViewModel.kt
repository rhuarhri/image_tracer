package com.rhuarhri.imagetracer.image_selection

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@HiltViewModel
class ImageSelectionViewModel @Inject constructor(
    private val repository: ImageSelectionRepository
) : ViewModel() {

    fun saveImage(image : Bitmap) = viewModelScope.launch {
        val outStream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 100, outStream)

        //save the new image
        repository.saveImage(outStream.toByteArray())

        //check for and delete any unnecessary images
        repository.delete()
    }
}