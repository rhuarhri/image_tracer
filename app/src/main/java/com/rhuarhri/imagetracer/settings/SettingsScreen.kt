package com.rhuarhri.imagetracer.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rhuarhri.imagetracer.ui.theme.ImageTracerTheme

object SettingsScreen {

    @Composable
    fun Screen(argument : String) {
        ImageTracerTheme {
            // A surface container using the 'background' color from the theme
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Text("Settings screen and $argument")
            }
        }
    }

}