package org.srinivas.siteworks.controller

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.srinivas.siteworks.changeservice.ChangeService
import org.srinivas.siteworks.data.PropertiesReadWriter
import org.srinivas.siteworks.denomination.Coin
import java.util.*
import java.util.stream.Collectors

@RestController
@RequestMapping(value = ["/change"])
open class ChangeController {

    @Autowired
    internal var propertiesReadWriter: PropertiesReadWriter? = null

    @Autowired
    internal var changeServiceImpl: ChangeService? = null

    @RequestMapping(value = ["/optimal"], method = [RequestMethod.GET])
    @ResponseBody
    fun handleGetOptimalCalculation(@RequestParam("pence") pence: Int?): List<Coin> {
        logger.info("ChangeController:handleGetOptimalCalculation Method")
        return try {
            propertiesReadWriter!!.readInventoryData()
            changeServiceImpl!!.getOptimalChangeFor(pence!!).stream().collect(Collectors.toList())
        } catch (e: Exception) {
            logger.info("Error:" + e.message)
            ArrayList()
        }

    }

    @RequestMapping(value = ["/supply"], method = [RequestMethod.GET])
    @ResponseBody
    fun handleGetSupplyCalculation(@RequestParam("pence") pence: Int?): List<Coin> {
        logger.info("ChangeController:handleGetSupplyCalculation Method")
        return try {
            propertiesReadWriter!!.readInventoryData()
            changeServiceImpl!!.getChangeFor(pence!!).stream().collect(Collectors.toList())
        } catch (e: Exception) {
            logger.info("Error:" + e.message)
            ArrayList()
        }

    }

    companion object {

        private val logger = LoggerFactory.getLogger(ChangeController::class.java)!!
    }

}
