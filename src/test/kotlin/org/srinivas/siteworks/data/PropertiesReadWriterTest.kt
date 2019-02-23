package org.srinivas.siteworks.data

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.runners.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class PropertiesReadWriterTest {
    private var propertiesReadWriter: PropertiesReadWriter? = null
    private var inventory: MutableMap<Int?, Int?>? = null
    @Mock
    internal var mockpropertiesReadWriter: PropertiesReadWriter? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        inventory = HashMap()
        inventory!![5] = 1000
        MockitoAnnotations.initMocks(this)
        propertiesReadWriter = PropertiesReadWriter()
        `when` (mockpropertiesReadWriter!!.inventoryMap).thenReturn(inventory)
        propertiesReadWriter!!.resourceName = FILE_NAME_TEST_COIN_INVENTORY_PROPERTIES
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        propertiesReadWriter = null
    }

    @Test
    @Throws(Exception::class)
    fun testReadInventoryData() {
        propertiesReadWriter!!.readInventoryData()
        assertTrue(propertiesReadWriter!!.inventoryMap!!.isNotEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun testWriteInventoryData() {
        propertiesReadWriter!!.writeInventoryData(inventory!!)
        propertiesReadWriter!!.readInventoryData()
        assertEquals(propertiesReadWriter!!.inventoryMap!!.size.toLong(), 1)
        assertTrue(propertiesReadWriter!!.inventoryMap!!.getValue(5) == 1000)
        propertiesReadWriter!!.writeInventoryData(CoinsInventoryData.inventoryData)
    }

    @Test
    @Throws(Exception::class)
    fun testDenominations() {
        assertTrue(propertiesReadWriter!!.denominations().isEmpty())
        propertiesReadWriter!!.readInventoryData()
        assertTrue(propertiesReadWriter!!.denominations().isNotEmpty())
        assertEquals(propertiesReadWriter!!.inventoryMap!!.keys.size.toLong(), propertiesReadWriter!!.denominations().size.toLong())
    }

    @Test
    fun testGetInventoryMap() {
        assertTrue(propertiesReadWriter!!.inventoryMap!!.isEmpty())
        propertiesReadWriter!!.inventoryMap = inventory
        assertEquals(propertiesReadWriter!!.inventoryMap!!.size.toLong(), 1)
       assertEquals(mockpropertiesReadWriter!!.inventoryMap!!.size.toLong(), 1)
    }

    @Test
    @Throws(Exception::class)
    fun testSetInventoryMap() {
        propertiesReadWriter!!.readInventoryData()
        assertTrue(propertiesReadWriter!!.inventoryMap!!.isNotEmpty())
    }

    companion object {
        const val FILE_NAME_TEST_COIN_INVENTORY_PROPERTIES = "test-coin-inventory.properties"
    }

}
