package org.srinivas.siteworks.controller

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration
import org.srinivas.siteworks.calculate.Calculate
import org.srinivas.siteworks.calculate.SupplyCalculator
import org.srinivas.siteworks.changeservice.ChangeServiceImpl
import org.srinivas.siteworks.config.AppConfig
import org.srinivas.siteworks.data.CoinsInventoryData
import org.srinivas.siteworks.data.PropertiesReadWriter
import org.srinivas.siteworks.denomination.Coin

@RunWith(SpringJUnit4ClassRunner::class)
@ContextConfiguration(classes = arrayOf(AppConfig::class))
@WebAppConfiguration
class ChangeControllerTest {

    private var changeContoller: ChangeController? = null
    private var supplyCalculator: SupplyCalculator? = null
    private var changeServiceImpl: ChangeServiceImpl? = null
    @Autowired
    private val optimalCalculator: Calculate? = null
    @Autowired
    internal var propertiesReadWriter: PropertiesReadWriter? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        supplyCalculator = SupplyCalculator()
        supplyCalculator!!.propertiesReadWriter = propertiesReadWriter
        propertiesReadWriter!!.resourceName = "test-coin-inventory.properties"
        propertiesReadWriter!!.writeInventoryData(CoinsInventoryData.inventoryData)
        changeServiceImpl = ChangeServiceImpl()
        changeServiceImpl!!.supplyCalculator = supplyCalculator
        changeServiceImpl!!.propertiesReadWriter = propertiesReadWriter
        changeServiceImpl!!.optimalCalculator = optimalCalculator
        changeContoller = ChangeController()
        changeContoller!!.changeServiceImpl = changeServiceImpl
        changeContoller!!.propertiesReadWriter = propertiesReadWriter
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        propertiesReadWriter!!.writeInventoryData(CoinsInventoryData.inventoryData)
        propertiesReadWriter = null
        supplyCalculator = null
        changeServiceImpl = null
    }

    @Test
    @Throws(Exception::class)
    fun testGetOptimalCalculation() {
        propertiesReadWriter!!.readInventoryData()
        val coins = changeContoller!!.handleGetOptimalCalculation(576)
        assertTrue(coins.size == 5)
        assertEquals(5, filterByValue(coins, 100).count!!.toInt().toLong())
        assertEquals(1, filterByValue(coins, 50).count!!.toInt().toLong())
        assertEquals(1, filterByValue(coins, 20).count!!.toInt().toLong())
        assertEquals(1, filterByValue(coins, 5).count!!.toInt().toLong())
        assertEquals(1, filterByValue(coins, 1).count!!.toInt().toLong())
    }

    @Test
    @Throws(Exception::class)
    fun testGetChangeFor() {
        propertiesReadWriter!!.readInventoryData()
        val coins = changeContoller!!.handleGetSupplyCalculation(2896)
        assertTrue(coins.size == 5)
        assertEquals(11, filterByValue(coins, 100).count!!.toInt().toLong())
        assertEquals(24, filterByValue(coins, 50).count!!.toInt().toLong())
        assertEquals(59, filterByValue(coins, 10).count!!.toInt().toLong())
        assertEquals(1, filterByValue(coins, 5).count!!.toInt().toLong())
        assertEquals(1, filterByValue(coins, 1).count!!.toInt().toLong())
    }

    private fun filterByValue(coins: Collection<Coin>, value: Int?): Coin {
        return coins.stream().filter { coin -> coin.value === value }.findFirst().get()
    }


}
