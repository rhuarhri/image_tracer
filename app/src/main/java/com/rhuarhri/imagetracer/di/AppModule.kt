package com.rhuarhri.imagetracer.di

import android.content.Context
import androidx.room.Room
import com.rhuarhri.imagetracer.botton_bar.ImageSelectionBottomBarRepository
import com.rhuarhri.imagetracer.botton_bar.ImageTracingBottomBarRepository
import com.rhuarhri.imagetracer.cameratrace.CameraTraceRepository
import com.rhuarhri.imagetracer.database.AdDao
import com.rhuarhri.imagetracer.database.AppDatabase
import com.rhuarhri.imagetracer.database.ImageDao
import com.rhuarhri.imagetracer.image_selection.ImageSelectionRepository
import com.rhuarhri.imagetracer.lightbox.LightBoxRepository
import com.rhuarhri.imagetracer.menu.MenuRepository
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
    fun provideImageSelectionRepository(imageDao : ImageDao) : ImageSelectionRepository {
        return ImageSelectionRepository(imageDao)
    }

    @Provides
    @Singleton
    fun provideCameraTraceRepository(imageDao: ImageDao) : CameraTraceRepository {
        return CameraTraceRepository(imageDao)
    }

    @Provides
    @Singleton
    fun provideLightBox(imageDao: ImageDao) : LightBoxRepository {
        return LightBoxRepository(imageDao)
    }

    @Provides
    @Singleton
    fun provideImageSelectionBottomBarRepository(imageDao: ImageDao) : ImageSelectionBottomBarRepository {
        return ImageSelectionBottomBarRepository(imageDao)
    }

    @Provides
    @Singleton
    fun provideImageTracingBottomBarRepository(imageDao: ImageDao) : ImageTracingBottomBarRepository {
        return ImageTracingBottomBarRepository(imageDao)
    }

    @Provides
    @Singleton
    fun provideMenuRepository(adDao: AdDao, imageDao: ImageDao) : MenuRepository {
        return MenuRepository(adDao, imageDao)
    }
}