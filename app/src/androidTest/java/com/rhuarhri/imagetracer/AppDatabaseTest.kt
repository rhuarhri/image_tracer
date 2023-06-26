package com.rhuarhri.imagetracer

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rhuarhri.imagetracer.botton_bar.DEFAULT_TRANSPARENCY
import com.rhuarhri.imagetracer.database.AdDao
import com.rhuarhri.imagetracer.database.EditingSettings
import com.rhuarhri.imagetracer.database.EditingSettingsDao
import com.rhuarhri.imagetracer.database.ImageDao
import com.rhuarhri.imagetracer.database.ImageEntity
import com.rhuarhri.imagetracer.database.TestAppDatabase
import com.rhuarhri.imagetracer.database.TestDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class AppDatabaseTest {

    private lateinit var appDatabase: TestAppDatabase
    private lateinit var adDao : AdDao
    private lateinit var imageDao: ImageDao
    private lateinit var settingsDao: EditingSettingsDao
    private lateinit var testDao: TestDao

    @Before
    fun setup() {
        val appContext = ApplicationProvider.getApplicationContext<Context>()
        appDatabase = Room.inMemoryDatabaseBuilder(
            appContext,
            TestAppDatabase::class.java
            ).allowMainThreadQueries().build()

        adDao = appDatabase.adDao()
        imageDao = appDatabase.imageDao()
        settingsDao = appDatabase.editingSettingsDao()
        testDao = appDatabase.testingDao()
    }

    @After
    fun close() {
        appDatabase.close()
    }

    private fun resetDatabase() {
        /*clear the data base before testing to ensure that no information added to the data bse
         in previous tests effect future test*/
        appDatabase.clearAllTables()
    }

    @Test
    fun insertSettingsTest() = runBlocking {

        resetDatabase()

        /*In this test there should only be one edit settings vale in the data base. Which should
         represent the settings of the last image the user was tracing.
         */

        settingsDao.insertSettings(EditingSettings(imageId = 1))

        //This should replace any existing edit settings value
        settingsDao.insertSettings(EditingSettings(imageId = 2))

        val settings = settingsDao.getSettings()

        val count = settings.size
        //there should only be one edit settings value
        assertEquals("edit settings count", 1, count)

        //This value should have the image id of 2
        val setting = settings.first()
        assertEquals("edit settings image id", 2, setting.imageId)

    }

    @Test
    fun checkAndUpdateSettingsTest() = runBlocking {

        resetDatabase()

        /*The role of the check and update settings function is that if the image changes. For
        example the user decides to trace a different image. Then the edit settings is reset for
        the new image.*/

        /*The data base should be empty. So with no edit settings the check and update settings
        function should create one with the image id of 1
         */
        settingsDao.checkAndUpdateSettings(1)

        var settings = settingsDao.getSettings()
        assertEquals("check and update on empty data base", 1, settings.first().imageId)

        settingsDao.insertSettings(EditingSettings(imageId = 1, transparency = 50))

        /*It should not change the settings if the image id matches what the edit settings has*/
        settingsDao.checkAndUpdateSettings(1)

        settings = settingsDao.getSettings()
        assertEquals("image id same in edit settings", 50, settings.first().transparency)

        /*Image id should not match the edit settings entity in the data base. As a result the
        edit settings should be reset.
         */
        settingsDao.checkAndUpdateSettings(2)

        settings = settingsDao.getSettings()
        assertEquals("image id same in edit settings", DEFAULT_TRANSPARENCY, settings.first().transparency)

    }

    /*Ads are not tested here as they have little functionality*/

    @Test
    fun getLatestImageTest() = runBlocking {
        resetDatabase()

        /*The image being displayed when the user is tracing should be the youngest image in the
        data base.
        * */

        //old image
        imageDao.addImage(ImageEntity(created = 1, data = "test"))

        //young image
        imageDao.addImage(ImageEntity(created = 2, data = "test"))

        val images = imageDao.getImage()

        /* At the moment the user can only trace one image at a time. As a result images should
        have the size of 1.
         */
        assertEquals("images size", 1, images.size)

        /*Only the youngest image should be returned*/
        assertEquals("image age", 2, images.first().created)
    }

    private fun setDefaultImages() = runBlocking {
        imageDao.addImage(ImageEntity(created = 1, data = "test"))
        imageDao.addImage(ImageEntity(created = 2, data = "test"))
        imageDao.addImage(ImageEntity(created = 3, data = "test"))
        imageDao.addImage(ImageEntity(created = 4, data = "test"))
        imageDao.addImage(ImageEntity(created = 5, data = "test"))
        imageDao.addImage(ImageEntity(created = 6, data = "test"))
        imageDao.addImage(ImageEntity(created = 7, data = "test"))
        imageDao.addImage(ImageEntity(created = 8, data = "test"))
        imageDao.addImage(ImageEntity(created = 9, data = "test"))
        imageDao.addImage(ImageEntity(created = 10, data = "test"))
        imageDao.addImage(ImageEntity(created = 11, data = "test"))
    }

    @Test
    fun getImageHistoryTest() = runBlocking {
        resetDatabase()

        /*
        The image history is used to display a list of past images that the user has traced. This
         gives the user to option to choose an image that they traced in the past.
         This history should only contain the last 10 images that the user has traced.
         */

        //This creates 11 images
        setDefaultImages()

        val images = imageDao.getImageHistory().first()

        assertEquals("history images list size", 10, images.size)

        /*
        This oldest image which has a created value of 1 should not be in this list.
         */

        assertNull("history no old images", images.find { it.created == 1L })
    }

    @Test
    fun deleteImagesTest() = runBlocking {
        resetDatabase()

        /*Since the user will only 10 images in the history, having any more images is pointless.
         So any image that is not part of the 10 images needed by the history will be deleted.
         */

        //This creates 11 images
        setDefaultImages()

        imageDao.unnecessaryImages().forEach {
            imageDao.deleteImage(it)
        }

        val images = testDao.getAllImages()

        /*There should be only 10 images in the data base when the delete function is called*/
        assertEquals("Delete images list size", 10, images.size)

        /*The image with the created value of 1 is the oldest image and should be deleted.*/
        assertNull("delete old images", images.find { it.created == 1L })
    }
}