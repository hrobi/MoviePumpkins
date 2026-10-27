package net.moviepumpkins.core.application.error

import net.moviepumpkins.core.shared.application.BusinessExceptionMessageResolver
import net.moviepumpkins.core.shared.application.ValidationErrorMessageResolver
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.Locale
import java.util.ResourceBundle

@Configuration(proxyBeanMethods = false)
class MessageResolversConfiguration {

    @Bean
    fun businessExceptionMessageResolver(): BusinessExceptionMessageResolver {
        val bundle = try {
            ResourceBundle.getBundle(
                "messages.error_messages",
                Locale.ENGLISH,
                ResourceBundle.Control.getNoFallbackControl(ResourceBundle.Control.FORMAT_PROPERTIES)
            )
        } catch (exception: Exception) {
            throw IllegalStateException(
                "Unable to load messages/error_messages_${Locale.ENGLISH.displayName}.properties",
                exception
            )
        }

        return BusinessExceptionMessageResolver(bundle)
    }

    @Bean
    fun validationErrorMessageResolver(): ValidationErrorMessageResolver {
        val bundle = try {
            ResourceBundle.getBundle(
                "messages.validation_error_messages",
                Locale.ENGLISH,
                ResourceBundle.Control.getNoFallbackControl(ResourceBundle.Control.FORMAT_PROPERTIES)
            )
        } catch (exception: Exception) {
            throw IllegalStateException(
                "Unable to load messages/error_messages_${Locale.ENGLISH.displayName}.properties",
                exception
            )
        }

        return ValidationErrorMessageResolver(bundle)
    }

}