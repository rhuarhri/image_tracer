package com.rhuarhri.imagetracer.cameratrace

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CameraTraceViewModel @Inject constructor(
    private val repository : CameraTraceRepository
) : ViewModel() {

}