package com.rhuarhri.imagetracer.di

import android.content.Context
import androidx.room.Room
import com.rhuarhri.imagetracer.cameratrace.CameraTraceRepository
import com.rhuarhri.imagetracer.cameratrace.CameraTraceRepositoryInterface
import com.rhuarhri.imagetracer.database.AdDao
import com.rhuarhri.imagetracer.database.AppDatabase
import com.rhuarhri.imagetracer.database.EditingSettingsDao
import com.rhuarhri.imagetracer.database.ImageDao
import com.rhuarhri.imagetracer.editing.EditingRepository
import com.rhuarhri.imagetracer.editing.EditingRepositoryInterface
import com.rhuarhri.imagetracer.image_selection.ImageSelectionRepository
import com.rhuarhri.imagetracer.image_selection.ImageSelectionRepositoryInterface
import com.rhuarhri.imagetracer.lightbox.LightBoxRepository
import com.rhuarhri.imagetracer.lightbox.LightBoxRepositoryInterface
import com.rhuarhri.imagetracer.menu.MenuRepository
import com.rhuarhri.imagetracer.menu.MenuRepositoryInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext applicationContext : Context
    ) : AppDatabase {
        return Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideImageDao(database : AppDatabase) : ImageDao {
        return database.imageDao()
    }

    @Provides
    @Singleton
    fun provideAdDao(database: AppDatabase) : AdDao {
        return database.adDao()
    }

    @Provides
    @Singleton
    fun provideEditingSettingsDao(database: AppDatabase) : EditingSettingsDao {
        return database.editingSettingsDao()
    }

    @Provides
    @Singleton
    fun provideImageSelectionRepository(imageDao : ImageDao) : ImageSelectionRepositoryInterface {
        return ImageSelectionRepository(imageDao)
    }

    @Provides
    @Singleton
    fun provideMenuRepository(adDao: AdDao, imageDao: ImageDao) : MenuRepositoryInterface {
        return MenuRepository(adDao, imageDao)
    }

    @Provides
    @Singleton
    fun provideEditingRepository(editingSettingsDao: EditingSettingsDao) : EditingRepositoryInterface {
        return EditingRepository(editingSettingsDao)
    }

    @Provides
    @Singleton
    fun provideLightBoxRepository(imageDao: ImageDao, editingSettingsDao: EditingSettingsDao)
            : LightBoxRepositoryInterface {
        return LightBoxRepository(imageDao, editingSettingsDao)
    }

    @Provides
    @Singleton
    fun provideCameraTraceRepository(imageDao: ImageDao, editingSettingsDao: EditingSettingsDao)
            : CameraTraceRepositoryInterface {
        return CameraTraceRepository(imageDao, editingSettingsDao)
    }
}