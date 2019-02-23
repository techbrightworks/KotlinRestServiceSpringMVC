package org.srinivas.siteworks.exception

import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doThrow
import org.mockito.MockitoAnnotations
import org.mockito.runners.MockitoJUnitRunner
import org.srinivas.siteworks.data.PropertiesReadWriter

@RunWith(MockitoJUnitRunner::class)
class CalculateExceptionTest {

    @Mock
    internal var mockpropertiesReadWriter: PropertiesReadWriter? = null


    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this)

    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        mockpropertiesReadWriter = null
    }

    @Test
    @Throws(Exception::class)
    fun testCalculateExceptionStringThrowable() {
        doThrow(CalculateException("Failed to Read or Write")).`when`<PropertiesReadWriter>(mockpropertiesReadWriter).readInventoryData()
        try {
            mockpropertiesReadWriter!!.readInventoryData()
            fail("Expected to throw CalculateException")
        } catch (e: Exception) {
            assertTrue(e is CalculateException)
        }

    }

    @Test
    @Throws(Exception::class)
    fun testCalculateExceptionString() {
        doThrow(CalculateException("Failed to Read or Write", RuntimeException("File Not Closed Properly"))).`when`<PropertiesReadWriter>(mockpropertiesReadWriter).readInventoryData()
        try {
            mockpropertiesReadWriter!!.readInventoryData()
            fail("Expected to throw CalculateException")
        } catch (e: Exception) {
            assertTrue(e is CalculateException)
            assertTrue(e.cause is RuntimeException)
        }

    }

}
