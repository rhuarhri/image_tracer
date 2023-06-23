package com.rhuarhri.imagetracer.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rhuarhri.imagetracer.botton_bar.DEFAULT_BRIGHTNESS
import com.rhuarhri.imagetracer.botton_bar.DEFAULT_BlUE
import com.rhuarhri.imagetracer.botton_bar.DEFAULT_CONTRAST
import com.rhuarhri.imagetracer.botton_bar.DEFAULT_GREEN
import com.rhuarhri.imagetracer.botton_bar.DEFAULT_INVERT
import com.rhuarhri.imagetracer.botton_bar.DEFAULT_LUMINANCE
import com.rhuarhri.imagetracer.botton_bar.DEFAULT_MONOCHROME
import com.rhuarhri.imagetracer.botton_bar.DEFAULT_RED
import com.rhuarhri.imagetracer.botton_bar.DEFAULT_TRANSPARENCY

@Entity(tableName = "settings_table")
data class EditingSettings(
    @PrimaryKey
    val id : Int = 0,
    val imageId : Int,
    var invert : Boolean = DEFAULT_INVERT,
    var red : Int = DEFAULT_RED, //as a percentage
    var green : Int = DEFAULT_GREEN, //as a percentage
    var blue : Int = DEFAULT_BlUE, //as a percentage
    var transparency : Int = DEFAULT_TRANSPARENCY, //as a percentage
    var contrast : Int = DEFAULT_CONTRAST,
    var brightness : Int = DEFAULT_BRIGHTNESS,
    var luminance : Int = DEFAULT_LUMINANCE,
    var isMonochrome : Boolean = DEFAULT_MONOCHROME
)