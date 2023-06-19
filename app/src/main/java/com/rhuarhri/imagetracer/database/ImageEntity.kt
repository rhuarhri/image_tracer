package com.rhuarhri.imagetracer.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/*At the moment there should be only one image in the
data base.
 */

@Entity(tableName = "image_table")
data class ImageEntity(
    @PrimaryKey
    val id : Int = 0,
    @ColumnInfo(name = "name") val name : String,
    //@ColumnInfo(name = "created") val created : Date,
    @ColumnInfo(name = "data", typeAffinity = ColumnInfo.BLOB)
    val data : ByteArray)