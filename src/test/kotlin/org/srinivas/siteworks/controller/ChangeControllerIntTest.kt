package org.srinivas.siteworks.controller

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import org.codehaus.jackson.map.ObjectMapper
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.srinivas.siteworks.config.AppConfig
import org.srinivas.siteworks.data.CoinsInventoryData
import org.srinivas.siteworks.data.PropertiesReadWriter

@RunWith(SpringJUnit4ClassRunner::class)
@ContextConfiguration(classes = arrayOf(AppConfig::class))
@WebAppConfiguration
class ChangeControllerIntTest {

    @Autowired
    internal var propertiesReadWriter: PropertiesReadWriter? = null

    @Autowired
    private val changeController: ChangeController? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        propertiesReadWriter!!.writeInventoryData(CoinsInventoryData.inventoryData)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        propertiesReadWriter!!.writeInventoryData(CoinsInventoryData.inventoryData)

    }

    @Test
    fun testChangeOptimalRestCall() {

        val mockMvc = MockMvcBuilders.standaloneSetup(this.changeController).build()
        try {

            val result = mockMvc.perform(MockMvcRequestBuilders.get("/change/optimal?pence=576"))
                    .andExpect(status().isOk).andReturn()

            val xmlMapper = XmlMapper()

            val coins = xmlMapper.readValue(result.response.contentAsString, List::class.java)
            assertTrue(coins.size == 5)
            val jsonMapper = ObjectMapper()
            jsonMapper.writeValueAsString(coins)

        } catch (e: Exception) {
            logger.info(e.message)
            fail("Failed Due to: " + e.message)
        }

    }

    @Test
    fun testChangeSuppyRestCall() {

        val mockMvc = MockMvcBuilders.standaloneSetup(this.changeController).build()
        try {

            val result = mockMvc.perform(MockMvcRequestBuilders.get("/change/optimal?pence=2896"))
                    .andExpect(status().isOk).andReturn()

            val xmlMapper = XmlMapper()
            val coins = xmlMapper.readValue(result.response.contentAsString, List::class.java)
            assertTrue(coins.size == 5)
            val jsonMapper = ObjectMapper()
            jsonMapper.writeValueAsString(coins)

        } catch (e: Exception) {
            logger.info(e.message)
            fail("Failed Due to: " + e.message)
        }

    }

    /**
     * The Class HelloWorldControllerTestConfiguration.
     */
    @Configuration
    internal open class ChangeControllerTestConfiguration {

        @Bean
        open fun changeController(): ChangeController {
            return ChangeController()
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ChangeControllerIntTest::class.java)!!
    }

}
