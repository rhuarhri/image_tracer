package com.rhuarhri.imagetracer.image_selection

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@HiltViewModel
class ImageSelectionViewModel @Inject constructor(
    private val repository: ImageSelectionRepositoryInterface
) : ViewModel() {

    fun saveImage(image : Bitmap) = viewModelScope.launch(Dispatchers.IO) {
        val outStream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 100, outStream)

        //save the new image
        repository.saveImage(outStream.toByteArray())

        //check for and delete any unnecessary images
        repository.delete()
    }
}