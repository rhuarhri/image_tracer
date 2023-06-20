package com.rhuarhri.imagetracer.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "image_table")
data class ImageEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    @ColumnInfo(name = "created") val created : Long = 0,
    @ColumnInfo(name = "data", typeAffinity = ColumnInfo.BLOB)
    val data : ByteArray)