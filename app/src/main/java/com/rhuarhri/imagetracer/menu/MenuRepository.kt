package com.rhuarhri.imagetracer.menu

import com.rhuarhri.imagetracer.database.AdDao
import com.rhuarhri.imagetracer.database.AdEntity
import com.rhuarhri.imagetracer.database.ImageDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MenuRepository @Inject constructor(
    private val adDao: AdDao, imageDao : ImageDao
) : MenuRepositoryInterface {

    override suspend fun increaseAd(count : Int) {
        adDao.updateAd(AdEntity(
            count = (count + 1)
        ))
    }

    override suspend fun resetAdCount() {
        adDao.updateAd(AdEntity(
            count = 0
        ))
    }

    /*Only the count is needed from the ad entity*/
    override val adCount = adDao.getAd().map {
        it.map { ad ->
            ad.count
        }
    }

    override val imagesExist = imageDao.doImagesExist()
}

interface MenuRepositoryInterface {

    suspend fun increaseAd(count : Int)

    suspend fun resetAdCount()

    val adCount : Flow<List<Int>>

    val imagesExist : Flow<Boolean>
}