package org.srinivas.siteworks.changeservice

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.srinivas.siteworks.calculate.Calculate
import org.srinivas.siteworks.data.PropertiesReadWriter
import org.srinivas.siteworks.denomination.Coin
import java.util.*


@Service
class ChangeServiceImpl : ChangeService {


    @Autowired
    var propertiesReadWriter: PropertiesReadWriter? = null


    @Autowired
    var supplyCalculator: Calculate? = null


    @Autowired
    var optimalCalculator: Calculate? = null

    /* (non-Javadoc)
	 * @see org.srinivas.siteworks.changeservice.ChangeService#getOptimalChangeFor(int)
	 */
    @Override
    override fun getOptimalChangeFor(pence: Int): Collection<Coin> {

        return try {
            optimalCalculator!!.calculate(pence, propertiesReadWriter!!.denominations())
        } catch (e: Exception) {
            log.error("Optimal Calculation not Successful", e)
            Collections.emptyList()
        }
    }

    /* (non-Javadoc)
	 * @see org.srinivas.siteworks.changeservice.ChangeService#getChangeFor(int)
	 */
    @Override
    override fun getChangeFor(pence: Int): Collection<Coin> {
        return try {
            supplyCalculator!!.calculate(pence, propertiesReadWriter!!.denominations())
        } catch (e: Exception) {
            log.error("Supply Calculation not Successful", e)
            Collections.emptyList()
        }

    }

    companion object {


        val log = LoggerFactory.getLogger(ChangeServiceImpl::class.java)!!
    }

}
