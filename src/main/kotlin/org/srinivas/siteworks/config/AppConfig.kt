package org.srinivas.siteworks.config


import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.FilterType
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer
import org.springframework.web.servlet.config.annotation.EnableWebMvc


@Configuration
@ComponentScan(basePackages = arrayOf("org.srinivas.siteworks"), excludeFilters = arrayOf(ComponentScan.Filter(value = [(EnableWebMvc::class)], type = FilterType.ANNOTATION)))
open class AppConfig {

    /**
     * Place holder configurer.
     *
     * @return the property sources placeholder configurer
     */
    @Bean
    open fun placeHolderConfigurer(): PropertySourcesPlaceholderConfigurer {
        val c = PropertySourcesPlaceholderConfigurer()
        c.setIgnoreUnresolvablePlaceholders(true)
        return c
    }
}
