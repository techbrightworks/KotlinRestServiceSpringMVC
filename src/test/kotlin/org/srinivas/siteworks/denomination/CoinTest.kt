package org.srinivas.siteworks.denomination

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class CoinTest {

    private var coin: Coin? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        coin = Coin()
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        coin = null
    }

    @Test
    fun testGetValue() {
        assertNull(coin!!.value)
        coin!!.value = 100
        assertEquals(100, coin!!.value!!.toInt().toLong())
    }

    @Test
    fun testSetValue() {
        coin!!.value = 100
        assertEquals(100, coin!!.value!!.toInt().toLong())
    }

    @Test
    fun testcount() {
        assertNull(coin!!.count)
        coin!!.count = 20
        assertEquals(20, coin!!.count!!.toInt().toLong())
    }

    @Test
    fun testSetCount() {
        coin!!.count = 20
        assertEquals(20, coin!!.count!!.toInt().toLong())
    }

    @Test
    fun testGetName() {
        assertNull(coin!!.name)
        coin!!.name = "One Pound"
        assertEquals("One Pound", coin!!.name)
    }

    @Test
    fun testSetName() {
        coin!!.name = "One Pound"
        assertEquals("One Pound", coin!!.name)
    }

}
