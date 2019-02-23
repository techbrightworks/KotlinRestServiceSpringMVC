/**
 * @author SrinivasJasti
 */
package org.srinivas.siteworks.webconfig

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.ViewResolver
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver

@Configuration
open class ChangeViewConfiguration {

    /**
     * viewResolver.
     *
     * @return the ViewResolver
     */
    @Bean
    open fun viewResolver(): ViewResolver {
        logger.info("ChangeViewConfiguration viewResolver()")
        return ContentNegotiatingViewResolver()
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ChangeViewConfiguration::class.java)!!
    }

}
