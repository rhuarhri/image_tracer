package com.rhuarhri.imagetracer.lightbox

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LightBoxViewModel @Inject constructor(
    private val repository : LightBoxRepository
) : ViewModel() {

}