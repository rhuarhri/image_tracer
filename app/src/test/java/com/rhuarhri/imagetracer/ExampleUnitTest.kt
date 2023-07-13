package com.rhuarhri.imagetracer

import org.junit.Test

import org.junit.Assert.*
import kotlin.math.floor

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun colourRange() {
        val red = 111
        val green = 100
        val blue = 10

        val factor = 1

        val redRound = (
                floor(
                    ((red / factor) / 10).toDouble()
                ).toInt() * factor * 10
                )

        val blueRound = (
                floor(
                    ((blue / factor) / 10).toDouble()
                ).toInt() * factor * 10
                )

        val greenRound = (
                floor(
                    ((green / factor) / 10).toDouble()
                ).toInt() * factor * 10
                )

        assertEquals("red test", 250, redRound)
        assertEquals("green test", 250, greenRound)
        assertEquals("blue test", 10, blueRound)
    }
}