package com.rhuarhri.imagetracer.database

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Query
import androidx.room.RoomDatabase

@Database(entities = arrayOf(ImageEntity::class, AdEntity::class, EditingSettings::class),
    version = 1, exportSchema = false)
abstract class TestAppDatabase : RoomDatabase() {

    abstract fun imageDao() : ImageDao

    abstract fun adDao() : AdDao

    abstract fun editingSettingsDao() : EditingSettingsDao

    abstract fun testingDao() : TestDao
}

/*This testing dao is used to query the data base for testing purposes.
This was created for the deleteImagesTest which needs to get all the images in the data base to
prove that the images are being deleted. However, there is no get all images query in the
imageDao. So one was added in the test dao.
 */
@Dao
interface TestDao {

    @Query("SELECT * FROM image_table")
    suspend fun getAllImages() : List<ImageEntity>
}