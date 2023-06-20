package com.rhuarhri.imagetracer.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface AdDao {

    /*
    There should only be one entity that contains the add count.
     */
    @Upsert
    suspend fun updateAd(adEntity: AdEntity)

    @Query("SELECT * FROM ad_table LIMIT 1")
    fun getAd() : Flow<List<AdEntity>>
}