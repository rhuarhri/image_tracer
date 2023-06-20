package com.rhuarhri.imagetracer.menu

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rhuarhri.imagetracer.utils.NetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(private val repository: MenuRepository) : ViewModel() {

    val adCount : StateFlow<List<Int>> = repository.adCount.stateIn(
        initialValue = listOf(0),
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000)
    )

    val imagesExist : StateFlow<Boolean> = repository.imagesExist.stateIn(
        initialValue = false,
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000)
    )

    private fun getCount() : Int {
        /*the count represents the number of times the user has used the app without watching an
        ad. For the moment if the app is used 5 times then they must watch an ad to continue.
         */

        val count = if (adCount.value.isEmpty()) {
            0
        } else {
            adCount.value.first()
        }

        return count
    }

    fun increaseAdCount() = viewModelScope.launch {
        repository.increaseAd(getCount())
    }

    fun resetAdCount() = viewModelScope.launch {
        if (getCount() >= 5) {
            repository.resetAdCount()
        }
    }

    fun canTraceImage() : Boolean {
        //this checks if the user is allowed to trace an image
        //or they have to watch an ad
        return getCount() < 5
    }

    fun checkConnection(context: Context): Boolean {
        return NetworkUtils.checkConnection(context)
    }
}