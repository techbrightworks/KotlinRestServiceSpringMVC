package org.srinivas.siteworks.calculate

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.srinivas.siteworks.data.PropertiesReadWriter
import org.srinivas.siteworks.denomination.Coin
import org.srinivas.siteworks.exception.CalculateException
import java.util.*
import java.util.concurrent.atomic.AtomicInteger


@Component
class SupplyCalculator : Calculate {


    @Autowired
    var propertiesReadWriter: PropertiesReadWriter? = null

    /* (non-Javadoc)
     * @see org.srinivas.siteworks.calculate.Calculate#calculate(java.lang.Integer, java.lang.Integer[])
     */
    @Override
    @Throws(Exception::class)
    override fun calculate(pence: Int?, denominations: Array<Int?>): List<Coin> {
        var suppliedCoins: MutableList<Coin>? = ArrayList()
        try {
            val inventoryMap = propertiesReadWriter!!.inventoryMap!!.toMutableMap()
            val inventoryTotal = inventoryMapValueTotal(inventoryMap)
            val shortSupplyList = ArrayList<Int?>()
            val remaining = AtomicInteger(pence!!.toInt())
            if (inventoryTotal!!.toInt() >= pence.toInt()) {
                supplyCalculation(denominations, inventoryMap, shortSupplyList, remaining, suppliedCoins!!)
                propertiesReadWriter!!.writeInventoryData(inventoryMap)
            }
        } catch (e: Exception) {
            suppliedCoins = ArrayList()
            log.error("SupplyCalculation Unsuccessful", e)
        }

        return suppliedCoins!!.toList()
    }

    /**
     * Supply calculation.
     *
     * @param denominations the denominations
     * @param inventoryMap the inventory map
     * @param shortSupplyList the short supply list
     * @param remaining the remaining
     * @param suppliedCoins the supplied coins
     * @throws Exception the exception
     */
    @Throws(Exception::class)
    private fun supplyCalculation(denominations: Array<Int?>, inventoryMap: MutableMap<Int?, Int?>, shortSupplyList: MutableList<Int?>, remaining: AtomicInteger, suppliedCoins: MutableList<Coin>) {
        val optimalCalculator = OptimalCalculator()
        if (denominations.isNullOrEmpty()) {
            throw CalculateException("Insufficient Supply of Coins")
        }
        val optimalCalculatedCoins = optimalCalculator.calculate(remaining.get(), denominations)
        calculateSupplyCoins(denominations, inventoryMap, shortSupplyList, remaining, suppliedCoins, optimalCalculatedCoins)
    }

    /**
     * Calculate supply coins.
     *
     * @param denominations the denominations
     * @param inventoryMap the inventory map
     * @param shortSupplyList the short supply list
     * @param remaining the remaining
     * @param suppliedCoins the supplied coins
     * @param optimalCalculatedCoins the optimal calculated coins
     * @throws Exception the exception
     */
    @Throws(Exception::class)
    private fun calculateSupplyCoins(denominations: Array<Int?>, inventoryMap: MutableMap<Int?, Int?>, shortSupplyList: MutableList<Int?>, remaining: AtomicInteger, suppliedCoins: MutableList<Coin>, optimalCalculatedCoins: List<Coin>) {
        Arrays.stream(denominations).forEach { key ->
            if (remaining.get() > 0 && denominations.isNotEmpty() && isOneofOptimalValue(optimalCalculatedCoins, key)) {

                try {
                    val coin = filterByValue(optimalCalculatedCoins, key)

                    if (coin != null && coin.count!!.toInt() > inventoryMap[key]!!.toInt()) {
                        insufficientInventoryChanges(inventoryMap, shortSupplyList, remaining, suppliedCoins, key)
                    } else {
                        inventoryAvailableChanges(inventoryMap, remaining, suppliedCoins, key, coin!!)
                    }
                    val denom = evaluateDenom(denominations, shortSupplyList, remaining)
                    recurseSupplyCalculation(inventoryMap, shortSupplyList, remaining, suppliedCoins, denom)
                } catch (e: Exception) {
                    throw RuntimeException("Insufficient Supply of Coins", e)
                }

            }
        }
    }

    /**
     * Evaluate denom.
     *
     * @param denominations the denominations
     * @param shortSupplyList the short supply list
     * @param remaining the remaining
     * @return the Int?[]
     */
    private fun evaluateDenom(denominations: Array<Int?>, shortSupplyList: MutableList<Int?>, remaining: AtomicInteger): Array<Int?> {
        var denom = arrayOf<Int?>()
        if (remaining.get() > 0 && shortSupplyList.size > 0) {
            denom = Arrays.stream(denominations).filter { den -> !shortSupplyList.contains(den) }.toArray { size -> arrayOfNulls<Int?>(size) }
        } else if (remaining.get() > 0 && shortSupplyList.size == 0 && denominations.isNotEmpty()) {
            denom = denominations
        } else {
            //do nothing
        }
        return denom
    }

    /**
     * Recurse supply calculation.
     *
     * @param inventoryMap the inventory map
     * @param shortSupplyList the short supply list
     * @param remaining the remaining
     * @param suppliedCoins the supplied coins
     * @param denom the denom
     * @throws CalculateException
     */
    @Throws(CalculateException::class)
    private fun recurseSupplyCalculation(inventoryMap: MutableMap<Int?, Int?>, shortSupplyList: MutableList<Int?>, remaining: AtomicInteger, suppliedCoins: MutableList<Coin>, denom: Array<Int?>) {
        if (remaining.get() > 0) {
            try {
                supplyCalculation(denom, inventoryMap, shortSupplyList, remaining, suppliedCoins)
            } catch (e: Exception) {
                log.error("SupplyCalculation Unsuccessful", e)
                throw CalculateException("Insufficient Supply of Coins", e)
            }

        }
    }

    /**
     * Insufficient inventory changes.
     *
     * @param inventoryMap the inventory map
     * @param shortSupplyList the short supply list
     * @param remaining the remaining
     * @param suppliedCoins the supplied coins
     * @param key the key
     */
    private fun insufficientInventoryChanges(inventoryMap: MutableMap<Int?, Int?>, shortSupplyList: MutableList<Int?>, remaining: AtomicInteger, suppliedCoins: MutableList<Coin>, key: Int?) {
        remaining.set(remaining.get() - inventoryMap[key]!!.toInt().times(key!!.toInt()))
        shortSupplyList.add(key)
        addCoin(key, inventoryMap[key], suppliedCoins)
        zeroValueInventory(inventoryMap, key)
    }

    /**
     * Inventory available changes.
     *
     * @param inventoryMap the inventory map
     * @param remaining the remaining
     * @param suppliedCoins the supplied coins
     * @param key the key
     * @param coin the coin
     */
    private fun inventoryAvailableChanges(inventoryMap: MutableMap<Int?, Int?>, remaining: AtomicInteger, suppliedCoins: MutableList<Coin>, key: Int?, coin: Coin) {
        reduceValueInventory(coin.count!!, inventoryMap, key)
        remaining.set(remaining.get() - coin.count!!.toInt().times(key!!.toInt()))
        addCoin(key, coin.count, suppliedCoins)
    }

    /**
     * Zero value inventory.
     *
     * @param inventoryMap the inventory map
     * @param key the key
     */
    private fun zeroValueInventory(inventoryMap: MutableMap<Int?, Int?>, key: Int?) {
        if (inventoryMap.containsKey(key)) {
            inventoryMap[key] = 0
        }
    }

    /**
     * Reduce value inventory.
     *
     * @param reduceBy the reducy by
     * @param inventoryMap the inventory map
     * @param key the key
     */
    private fun reduceValueInventory(reduceBy: Int?, inventoryMap: MutableMap<Int?, Int?>, key: Int?) {
        if (inventoryMap.containsKey(key)) {
            inventoryMap[key] = inventoryMap[key]!!.minus(reduceBy!!.toInt())
        }
    }

    /**
     * Filter by value.
     *
     * @param coins the coins
     * @param value the value
     * @return the coin
     * @throws Exception the exception
     */
    @Throws(Exception::class)
    private fun filterByValue(coins: List<Coin>, value: Int?): Coin? {
        return coins.stream().filter {
            it.value === value
        }.findFirst().get()

    }

    /**
     * Checks if is oneof optimal value.
     *
     * @param coins the coins
     * @param value the value
     * @return true, if is oneof optimal value
     */
    private fun isOneofOptimalValue(coins: List<Coin>, value: Int?): Boolean {

        return coins.stream().anyMatch { coin -> coin.value === value }
    }

    /**
     * Adds the coin.
     *
     * @param value the value
     * @param count the count
     * @param coins the coins
     */
    private fun addCoin(value: Int?, count: Int?, coins: MutableList<Coin>) {
        val coin = Coin()
        coin.value = value
        coin.count = count
        if (count!!.toInt() > 0) {
            coins.add(coin)
        }
    }

    /**
     * Inventory map value total.
     *
     * @param inventoryMap the inventory map
     * @return the integer
     */
    private fun inventoryMapValueTotal(inventoryMap: Map<Int?, Int?>): Int? {
        val total = AtomicInteger(0)
        inventoryMap.entries.stream().forEach { e ->
            total.set(total.get() + e.key!!.toInt().times(e.value!!.toInt()))
        }
        return total.get()
    }

    companion object {

        val log = LoggerFactory.getLogger(SupplyCalculator::class.java)!!
    }

}