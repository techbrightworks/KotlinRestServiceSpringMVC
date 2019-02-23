package org.srinivas.siteworks.calculate

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
import org.srinivas.siteworks.config.AppConfig
import org.srinivas.siteworks.data.PropertiesReadWriter
import org.srinivas.siteworks.denomination.Coin

@RunWith(SpringJUnit4ClassRunner::class)
@ContextConfiguration(classes = arrayOf(AppConfig::class))
@WebAppConfiguration
class OptimalCalculatorTest {

    @Autowired
    private var optimalCalculator: Calculate? = null

    @Autowired
    internal var propertiesReadWriter: PropertiesReadWriter? = null


    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this)

    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        propertiesReadWriter = null
        optimalCalculator = null
    }

    @Test
    @Throws(Exception::class)
    fun testCalculate() {
        propertiesReadWriter!!.readInventoryData()
        val coins = optimalCalculator!!.calculate(576, propertiesReadWriter!!.denominations())
        assertTrue(coins.size == 5)
        assertEquals(5, filterByValue(coins, 100).count!!.toInt().toLong())
        assertEquals(1, filterByValue(coins, 50).count!!.toInt().toLong())
        assertEquals(1, filterByValue(coins, 20).count!!.toInt().toLong())
        assertEquals(1, filterByValue(coins, 5).count!!.toInt().toLong())
        assertEquals(1, filterByValue(coins, 1).count!!.toInt().toLong())
    }

    @Test
    @Throws(Exception::class)
    fun testErrorScenario() {
        val emptyArray = arrayOf<Int?>()
        propertiesReadWriter!!.readInventoryData()
        val coins = optimalCalculator!!.calculate(576, emptyArray)
        assertTrue(coins.isEmpty())
    }


    private fun filterByValue(coins: List<Coin>, value: Int?): Coin {
        return coins.stream().filter { coin -> coin.value === value }.findFirst().get()
    }

}
