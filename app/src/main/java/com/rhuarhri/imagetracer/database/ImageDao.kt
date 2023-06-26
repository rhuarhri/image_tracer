package com.rhuarhri.imagetracer.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addImage(imageEntity: ImageEntity)

    /*get the last image to have entered the data base*/
    @Query("SELECT * FROM image_table ORDER BY created DESC LIMIT 1")
    suspend fun getImage() : List<ImageEntity>

    @Query("SELECT EXISTS(SELECT * FROM image_table)")
    fun doImagesExist() : Flow<Boolean>

    /*Get the top 10 youngest images in the data base*/
    @Query("SELECT * FROM image_table ORDER BY created DESC LIMIT 10")
    fun getImageHistory() : Flow<List<ImageEntity>>

    /*This will delete an image that is older than the youngest 10 images.
    * This ensures that the app is not full of images that the user doe not need.*/
    @Query("SELECT * FROM image_table WHERE id NOT IN(" +
            "SELECT id FROM image_table ORDER BY created DESC LIMIT 10" +
            ")")
    suspend fun unnecessaryImages() : List<ImageEntity>

    @Delete
    suspend fun deleteImage(imageEntity: ImageEntity)
}