package com.rhuarhri.imagetracer.botton_bar

import androidx.lifecycle.ViewModel
import com.rhuarhri.imagetracer.botton_bar.widgets.BottomBarWidget
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class BottomBarViewModel : ViewModel() {
    private val _isBarVisible : MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isBarVisible : StateFlow<Boolean> = _isBarVisible

    /*The selected item is the selected option in the bottom bar*/
    private val _selectedItem : MutableStateFlow<BottomBarWidget?> = MutableStateFlow(null)
    val selectedItem: StateFlow<BottomBarWidget?> = _selectedItem

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

    fun hideExtension() {
        _selectedItem.update {
            null
        }
    }

    fun setSelectedItem(item : BottomBarWidget) {
        _selectedItem.update {
            item
        }
    }
}