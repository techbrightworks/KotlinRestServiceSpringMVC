package org.srinivas.siteworks.calculate

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.srinivas.siteworks.denomination.Coin
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

/**
 * The Class OptimalCalculator.
 */
@Component
class OptimalCalculator : Calculate {

    /* (non-Javadoc)
     * @see org.srinivas.siteworks.calculate.Calculate#calculate(java.lang.Integer, java.lang.Integer[])
     */
    @Override
    override fun calculate(pence: Int?, denominations: Array<Int?>): List<Coin> {
        val coinsMap = ArrayList<Coin>()
        try {
            val remainingPence = AtomicInteger(pence!!.toInt())
            optimalCalculation(denominations, coinsMap, remainingPence)
        } catch (e: Exception) {
            log.error("OptimalCalculation Unsuccessful", e)
        }

        return coinsMap
    }

    /**
     * Optimal calculation.
     *
     * @param denominations the denominations
     * @param coinsMap the coins map
     * @param remainingPence the remaining pence
     */
    private fun optimalCalculation(denominations: Array<Int?>, coinsMap: MutableList<Coin>, remainingPence: AtomicInteger) {
        Arrays.stream(denominations).forEach { denomination ->
            if (remainingPence.get() > 0) {
                denominationCalculation(remainingPence.get(), coinsMap, denomination)
                remainingPence.set(remainingCalculation(remainingPence.get(), denomination)!!.toInt())
            }
        }
    }

    /**
     * Denomination calculation.
     *
     * @param pence the pence
     * @param coinsMap the coins map
     * @param denomination the denomination
     */
    private fun denominationCalculation(pence: Int?, coinsMap: MutableList<Coin>, denomination: Int?) {
        val coins = Math.floorDiv(pence!!.toInt(), denomination!!.toInt())
        val coin = Coin()
        coin.value = denomination
        coin.count = coins
        addToCoinsList(coinsMap, coins, coin)
    }

    /**
     * Adds the to coins list.
     *
     * @param coinsList the coins List
     * @param coins the coins
     * @param coin the coin
     */
    private fun addToCoinsList(coinsList: MutableList<Coin>, coins: Int?, coin: Coin) {
        if (coins!!.toInt() > 0) {
            coinsList.add(coin)
        }
    }

    /**
     * Remaining Pence calculation.
     *
     * @param pence the pence
     * @param denomination the denomination
     * @return the integer
     */
    private fun remainingCalculation(pence: Int?, denomination: Int?): Int? {
        return pence!!.toInt() % denomination!!.toInt()

    }

    companion object {


        val log = LoggerFactory.getLogger(OptimalCalculator::class.java)!!
    }
}