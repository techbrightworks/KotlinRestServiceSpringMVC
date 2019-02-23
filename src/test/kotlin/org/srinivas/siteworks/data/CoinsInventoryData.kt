package org.srinivas.siteworks.data

import java.util.*

object CoinsInventoryData {

    /**
     * Gets the special offers data.
     *
     * @return the special offers data
     */
    val inventoryData: Map<Int?, Int?>
        get() {
            val inventory = Hashtable<Int?, Int?>()
            inventory[100] = 11
            inventory[50] = 24
            inventory[20] = 0
            inventory[10] = 99
            inventory[5] = 200
            inventory[2] = 11
            inventory[1] = 23
            return inventory
        }

}
