package com.rhuarhri.imagetracer.database


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ad_table")
data class AdEntity(
    @PrimaryKey
    val id : Int = 0,
    val count : Int = 0
)