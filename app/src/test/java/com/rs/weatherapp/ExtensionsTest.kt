package com.rs.weatherapp

import org.junit.Assert
import org.junit.Test

class ExtensionsTest {

    @Test
    fun testVerifyPermissions() {
        Assert.assertTrue(intArrayOf(0, 0).verifyPermissions())
        Assert.assertFalse(intArrayOf(1, 0).verifyPermissions())
        Assert.assertFalse(intArrayOf().verifyPermissions())
    }

    @Test
    fun testToDate() {
        Assert.assertNotNull(1577495460L.toDate())
        Assert.assertTrue(1577495460L.toDate()?.contains("2019-12-28") == true)
    }

    @Test
    fun testToTime() {

        // TODO This test depends on time zone
        Assert.assertNotNull(1577495460L.toTime())
        Assert.assertTrue(1577495460L.toTime()?.contains("06:41:00") == true)
    }
}