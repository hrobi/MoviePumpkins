package net.moviepumpkins.core.shared.application

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.Locale
import java.util.ResourceBundle

@Configuration(proxyBeanMethods = false)
class BusinessExceptionMessageConfiguration {

    @Bean
    fun businessExceptionMessageResolver(): BusinessExceptionMessageResolver {
        val bundle = try {
            ResourceBundle.getBundle(
                "messages.error_messages",
                Locale.ENGLISH,
                ResourceBundle.Control.getNoFallbackControl(ResourceBundle.Control.FORMAT_PROPERTIES)
            )
        } catch (exception: Exception) {
            throw IllegalStateException("Unable to load messages/error_messages_en.properties", exception)
        }

        return BusinessExceptionMessageResolver(bundle)
    }

}
