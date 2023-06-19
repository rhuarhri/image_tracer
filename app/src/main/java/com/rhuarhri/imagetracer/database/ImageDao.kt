package com.rhuarhri.imagetracer.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDao {

    /*
    Might be useful in the future when there are more images in the data base
    @Query("SELECT * FROM image_table ORDER BY created ASC LIMIT 1")
    fun getFirst() : ImageEntity*/

    @Upsert
    suspend fun addImage(imageEntity: ImageEntity)

    @Query("SELECT * FROM image_table LIMIT 1")
    suspend fun getImage() : List<ImageEntity>

    @Query("SELECT EXISTS(SELECT * FROM image_table)")
    fun doImagesExist() : Flow<Boolean>
}