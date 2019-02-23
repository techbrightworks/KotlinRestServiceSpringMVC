package org.srinivas.siteworks.calculate

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.doThrow
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration
import org.srinivas.siteworks.config.AppConfig
import org.srinivas.siteworks.data.CoinsInventoryData
import org.srinivas.siteworks.data.PropertiesReadWriter
import org.srinivas.siteworks.denomination.Coin

@RunWith(SpringJUnit4ClassRunner::class)
@ContextConfiguration(classes = arrayOf(AppConfig::class))
@WebAppConfiguration
class SupplyCalculatorTest {

    private var supplyCalculator: SupplyCalculator? = null

    @Autowired
    internal var propertiesReadWriter: PropertiesReadWriter? = null
    @Mock
    internal var mockpropertiesReadWriter: PropertiesReadWriter? = null


    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        supplyCalculator = SupplyCalculator()
        supplyCalculator!!.propertiesReadWriter = propertiesReadWriter
        propertiesReadWriter!!.resourceName = "test-coin-inventory.properties"
        propertiesReadWriter!!.writeInventoryData(CoinsInventoryData.inventoryData)
        `when`<String>(mockpropertiesReadWriter!!.resourceName).thenReturn("resource")
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        propertiesReadWriter!!.writeInventoryData(CoinsInventoryData.inventoryData)
        propertiesReadWriter = null
        supplyCalculator = null
    }

    @Test
    @Throws(Exception::class)
    fun testCalculateBasedOnSufficientSupply() {
        propertiesReadWriter!!.readInventoryData()
        val coins = supplyCalculator!!.calculate(2896, propertiesReadWriter!!.denominations())
        assertTrue(coins.size == 5)
        assertEquals(11, filterByValue(coins, 100).count!!.toInt().toLong())
        assertEquals(24, filterByValue(coins, 50).count!!.toInt().toLong())
        assertEquals(59, filterByValue(coins, 10).count!!.toInt().toLong())
        assertEquals(1, filterByValue(coins, 5).count!!.toInt().toLong())
        assertEquals(1, filterByValue(coins, 1).count!!.toInt().toLong())
    }

    @Test
    @Throws(Exception::class)
    fun testCalculateBasedInSufficientSecondSupply() {
        propertiesReadWriter!!.readInventoryData()
        var coins = supplyCalculator!!.calculate(2896, propertiesReadWriter!!.denominations())
        assertTrue(coins.size == 5)
        assertEquals(11, filterByValue(coins, 100).count!!.toInt().toLong())
        assertEquals(24, filterByValue(coins, 50).count!!.toInt().toLong())
        assertEquals(59, filterByValue(coins, 10).count!!.toInt().toLong())
        assertEquals(1, filterByValue(coins, 5).count!!.toInt().toLong())
        assertEquals(1, filterByValue(coins, 1).count!!.toInt().toLong())
        coins = supplyCalculator!!.calculate(6, propertiesReadWriter!!.denominations())
        assertTrue(coins.size == 2)
        assertEquals(1, filterByValue(coins, 5).count!!.toInt().toLong())
        assertEquals(1, filterByValue(coins, 1).count!!.toInt().toLong())
    }

    @Test
    @Throws(Exception::class)
    fun testCalculateBasedOnInSufficientSupply() {
        propertiesReadWriter!!.readInventoryData()
        val coins = supplyCalculator!!.calculate(5000, propertiesReadWriter!!.denominations())
        assertTrue(coins.isEmpty())
    }

    @Test
    fun testGetPropertiesReadWriter() {
        assertEquals(supplyCalculator!!.propertiesReadWriter!!.resourceName, FILE_NAME_TEST_COIN_INVENTORY_PROPERTIES)
    }

    @Test
    fun testSetPropertiesReadWriter() {
        supplyCalculator!!.propertiesReadWriter = mockpropertiesReadWriter
        assertEquals(supplyCalculator!!.propertiesReadWriter!!.resourceName, "resource")
    }

    @Test
    @Throws(Exception::class)
    fun testErrorScenario() {
        supplyCalculator!!.propertiesReadWriter = mockpropertiesReadWriter
        doThrow(RuntimeException("Failed to Read or Write")).`when`<PropertiesReadWriter>(mockpropertiesReadWriter).inventoryMap
        propertiesReadWriter!!.readInventoryData()
        val coins = supplyCalculator!!.calculate(2896, propertiesReadWriter!!.denominations())
        assertTrue(coins.isEmpty())
    }


    private fun filterByValue(coins: List<Coin>, value: Int?): Coin {
        return coins.stream().filter { coin -> coin.value === value }.findFirst().get()
    }

    companion object {
        const val FILE_NAME_TEST_COIN_INVENTORY_PROPERTIES = "test-coin-inventory.properties"
    }

}
