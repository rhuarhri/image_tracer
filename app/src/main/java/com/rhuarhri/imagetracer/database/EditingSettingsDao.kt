package com.rhuarhri.imagetracer.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface EditingSettingsDao {

    @Upsert
    suspend fun insertSettings(settings : EditingSettings)

    suspend fun checkAndUpdateSettings(imageId: Int) {
        /*if the settings relate to an image don't exist.
        This could be because there are settings or a different image or there are no settings.
        Then settings for this image need to be created and added to the data base
         */
        if (!settingsExist(imageId)) {
            insertSettings(EditingSettings(imageId = imageId))
        }
    }

    @Query("UPDATE settings_table SET " +
            "invert = :invert, " +
            "red = :red, " +
            "green = :green, " +
            "blue = :blue, " +
            "transparency = :transparency, " +
            "contrast = :contrast, " +
            "brightness = :brightness, " +
            "luminance = :luminance, " +
            "isMonochrome = :isMonochrome" +
            " WHERE id = 0")
    suspend fun updateSettings(
        invert : Boolean,
        red : Int,
        green : Int,
        blue : Int,
        transparency : Int,
        contrast : Int,
        brightness : Int,
        luminance : Int,
        isMonochrome : Boolean
    )

    @Query("SELECT EXISTS(SELECT * FROM settings_table WHERE imageId = :imageId)")
    suspend fun settingsExist(imageId : Int) : Boolean

    //This gets the settings for the last image to be added to the data base. The last image will
    // be the image the user has just selected or the image they were last tracing.
    /*@Query("SELECT * FROM settings_table WHERE imageId IN(SELECT id FROM image_table ORDER BY created DESC LIMIT 1)")*/

    /*There should be only one entity in the settings table and it should have the id of 0*/
    @Query("SELECT * FROM settings_table WHERE id = 0")
    suspend fun getSettings() : List<EditingSettings>
}