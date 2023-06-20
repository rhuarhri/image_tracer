package com.rhuarhri.imagetracer.menu

import com.rhuarhri.imagetracer.database.AdDao
import com.rhuarhri.imagetracer.database.AdEntity
import com.rhuarhri.imagetracer.database.ImageDao
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MenuRepository @Inject constructor(private val adDao: AdDao, imageDao : ImageDao) {

    suspend fun increaseAd(count : Int) {
        adDao.updateAd(AdEntity(
            count = (count + 1)
        ))
    }

    suspend fun resetAdCount() {
        adDao.updateAd(AdEntity(
            count = 0
        ))
    }

    /*Only the count is needed from the ad entity*/
    val adCount = adDao.getAd().map {
        it.map { ad ->
            ad.count
        }
    }

    val imagesExist = imageDao.doImagesExist()
}