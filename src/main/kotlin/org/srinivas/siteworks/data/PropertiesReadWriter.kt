package org.srinivas.siteworks.data

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import org.srinivas.siteworks.exception.CalculateException
import java.io.FileInputStream
import java.io.FileWriter
import java.io.IOException
import java.util.*
import java.util.stream.Collectors

@Repository
open class PropertiesReadWriter {

    open var inventoryMap: Map<Int?, Int?>? = null
        get() {
            if (field == null) {
                this.inventoryMap = HashMap()
            }
            return field
        }

    open var resourceName: String? = null
        get() {
            if (field == null) {
                resourceName = FILE_NAME_COIN_INVENTORY_PROPERTIES
            }
            return field
        }

    /**
     * Read inventory data.
     *
     * @throws Exception the exception
     */
    @Throws(Exception::class)
    open fun readInventoryData() {
        val url = this::class.java.classLoader.getResource(resourceName)
        val prop = Properties()
        val input = FileInputStream(url.path)
        try {
            prop.load(input)
            val inventoryData = extractInventoryData(prop)
            inventoryMap = inventoryData
        } catch (ex: IOException) {
            log.error("Unable to Read Sucesssfully", ex)
            throw CalculateException("Unable to Read Sucesssfully", ex)
        } finally {
            try {
                when {
                    input != null -> input.close()
                }
            } catch (ex: IOException) {
                log.error("InputStream Not Closed", ex)
                throw CalculateException("InputStream Not Closed", ex)
            }

        }
    }

    /**
     * Write inventory data.
     *
     * @param inventoryMap the inventory map
     * @throws Exception the exception
     */
    @Throws(Exception::class)
    open fun writeInventoryData(inventoryMap: Map<Int?, Int?>) {
        val url = this::class.java.classLoader.getResource(resourceName)
        val prop = Properties()
        val writer = FileWriter(url.path)
        try {
            inventoryMap.entries.stream().forEach { coin ->
                prop.setProperty(coin.key.toString(), coin.value.toString())
            }
            prop.store(writer, COIN_INVENTORY_INFORMATION)
            writer.close()
        } catch (ex: IOException) {
            log.error("Unable to Write Sucesssfully", ex)
            throw CalculateException("Unable to Read Sucesssfully", ex)
        } finally {

            try {
                when {
                    writer != null -> writer.close()
                }
            } catch (ex: IOException) {
                log.error("Writer Not Closed", ex)
                throw CalculateException("Writer Not Closed", ex)
            }

        }
    }

    /**
     * Denominations.
     *
     * @return the Int?[]
     */
    fun denominations(): Array<Int?> {
        val denominations = inventoryMap!!.keys.stream().toArray { size -> arrayOfNulls<Int?>(size) }
        Arrays.sort(denominations, Collections.reverseOrder())
        return denominations
    }

    /**
     * Extract inventory data.
     *
     * @param prop the prop
     * @return the map
     */
    private fun extractInventoryData(prop: Properties): Map<Int?, Int?> {
        return prop.entries.stream()
                .collect(Collectors.toMap({ e -> Integer.valueOf(e.key.toString()) }, { e -> Integer.valueOf(e.value.toString()) }))

    }

    companion object {

        const val COIN_INVENTORY_INFORMATION = "Coin-Inventory Information"
        const val FILE_NAME_COIN_INVENTORY_PROPERTIES = "coin-inventory.properties"
        val log = LoggerFactory.getLogger(PropertiesReadWriter::class.java)!!
    }

}
