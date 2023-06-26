package com.rhuarhri.imagetracer.image_selection

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageSelectionViewModel @Inject constructor(
    private val repository: ImageSelectionRepositoryInterface
) : ViewModel() {

    fun saveImage(image : Bitmap, context : Context) = viewModelScope.launch(Dispatchers.IO) {

        //save the new image
        repository.saveImage(context, image)

        //check for and delete any unnecessary images
        repository.delete()
    }
}