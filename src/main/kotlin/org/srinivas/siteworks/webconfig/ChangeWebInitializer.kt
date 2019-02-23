/**
 * @author SrinivasJasti
 */
package org.srinivas.siteworks.webconfig

import org.slf4j.LoggerFactory
import org.springframework.web.WebApplicationInitializer
import org.springframework.web.context.ContextLoaderListener
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext
import org.springframework.web.servlet.DispatcherServlet
import javax.servlet.ServletContext
import javax.servlet.ServletRegistration

/**
 * The Class ChangeWebInitializer.
 */

open class ChangeWebInitializer : WebApplicationInitializer {

    /* (non-Javadoc)
	 * @see org.springframework.web.WebApplicationInitializer#onStartup(javax.servlet.ServletContext)
	 */
    @Override
    override fun onStartup(container: ServletContext) {
        logger.info("Started to pickup the annotated classes at ChangeWebInitializer")
        startServlet(container)
    }

    /**
     * Start servlet.
     *
     * @param container the container
     */
    private fun startServlet(container: ServletContext) {
        val dispatcherContext = registerContext(ChangeMvcContextConfiguration::class.java)
        val dispatcherServlet = DispatcherServlet(dispatcherContext)
        container.addListener(ContextLoaderListener(dispatcherContext))
        val dispatcher: ServletRegistration.Dynamic
        dispatcher = container.addServlet("dispatcher", dispatcherServlet)
        dispatcher.setLoadOnStartup(1)
        dispatcher.addMapping("/")
    }

    /**
     * Register context.
     *
     * @param annotatedClasses the annotated classes
     * @return the web application context
     */
    private fun registerContext(vararg annotatedClasses: Class<*>): WebApplicationContext {
        logger.info("Using AnnotationConfigWebApplicationContext createContext")
        val context = AnnotationConfigWebApplicationContext()
        context.register(*annotatedClasses)
        return context
    }

    companion object {

        private val logger = LoggerFactory.getLogger(ChangeWebInitializer::class.java)!!
    }

}

