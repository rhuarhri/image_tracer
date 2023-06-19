package com.rhuarhri.imagetracer.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(ImageEntity::class, AdEntity::class), version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun imageDao() : ImageDao

    abstract fun adDao() : AdDao

}