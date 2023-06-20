package com.rhuarhri.imagetracer.botton_bar

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

abstract class BottomBarViewModel : ViewModel() {
    private val _isExtensionVisible : MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isExtensionVisible : StateFlow<Boolean> = _isExtensionVisible

    private val _isBarVisible : MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isBarVisible : StateFlow<Boolean> = _isBarVisible

    /*The selected item is the selected option in the bottom bar*/
    private val _selectedItem : MutableStateFlow<BottomBarItem?> = MutableStateFlow(null)
    val selectedItem: StateFlow<BottomBarItem?> = _selectedItem

    fun show() {
        _isBarVisible.update {
            true
        }
    }

    fun hide() {

        _isBarVisible.update {
            false
        }
    }

    fun showExtension() {
        _isExtensionVisible.update {
            true
        }
    }

    fun hideExtension() {
        _isExtensionVisible.update {
            false
        }

        _selectedItem.update {
            null
        }
    }

    fun setSelectedItem(item : BottomBarItem) {
        _selectedItem.update {
            item
        }
    }
}