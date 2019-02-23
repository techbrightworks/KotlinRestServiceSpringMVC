package org.srinivas.siteworks.changeservice

import org.srinivas.siteworks.denomination.Coin


interface ChangeService {

    /**
     * Gets the optimal change for.
     *
     * @param pence the pence
     * @return the optimal change for
     */
    fun getOptimalChangeFor(pence: Int): Collection<Coin>

    /**
     * Gets the change for.
     *
     * @param pence the pence
     * @return the change for
     */
    fun getChangeFor(pence: Int): Collection<Coin>


}
