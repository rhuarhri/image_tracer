package com.rhuarhri.imagetracer

import com.rhuarhri.imagetracer.database.AdDao
import com.rhuarhri.imagetracer.database.AdEntity
import com.rhuarhri.imagetracer.database.ImageDao
import com.rhuarhri.imagetracer.database.ImageEntity
import com.rhuarhri.imagetracer.menu.MenuRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class MenuRepositoryTests {

    companion object {
        //There maybe multiple versions of TestAppDao and TestImageDao so they are placed in a
        // object to identify them.
        class TestAppDao : AdDao {
            override suspend fun updateAd(adEntity: AdEntity) {
                //do nothing
            }

            override fun getAd(): Flow<List<AdEntity>> {
                return flow {
                    emit(
                        listOf(
                            AdEntity(
                                0,
                                1
                            )
                        )
                    )
                }
            }
        }

        class TestImageDao : ImageDao {
            override suspend fun addImage(imageEntity: ImageEntity) {
                //do nothing
            }

            override suspend fun getImage(): List<ImageEntity> {
                return emptyList()
            }

            override fun doImagesExist(): Flow<Boolean> {
                return flow {
                    emit(true)
                }
            }

            override fun getImageHistory(): Flow<List<ImageEntity>> {
                //do nothing
                return flow {
                    emit(emptyList())
                }
            }

            override suspend fun unnecessaryImages(): List<ImageEntity> {
                return emptyList()
            }

            override suspend fun deleteImage(imageEntity: ImageEntity) {
                //do nothing
            }

        }
    }

    @Test
    fun getAdCountTest() = runBlocking {
        val menuRepo = MenuRepository(TestAppDao(), TestImageDao())

        /*The app only needs the ad count from the data base and this check that this is all the
        app is getting.
         */
        val count = menuRepo.adCount.first()

        Assert.assertEquals("ad count", 1, count.first())
    }
}