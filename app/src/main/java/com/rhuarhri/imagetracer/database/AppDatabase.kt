package com.rhuarhri.imagetracer.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(ImageEntity::class, AdEntity::class, EditingSettings::class),
    version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun imageDao() : ImageDao

    abstract fun adDao() : AdDao

    abstract fun editingSettingsDao() : EditingSettingsDao

}