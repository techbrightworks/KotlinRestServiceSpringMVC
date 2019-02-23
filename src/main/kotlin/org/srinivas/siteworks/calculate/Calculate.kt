package org.srinivas.siteworks.calculate

import org.srinivas.siteworks.denomination.Coin


interface Calculate {

    /**
     * Calculate.
     *
     * @param pence the pence
     * @param denominations the denominations
     * @return the list
     * @throws Exception the exception
     */
    @Throws(Exception::class)
    fun calculate(pence: Int?, denominations: Array<Int?>): List<Coin>

}
