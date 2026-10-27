package net.moviepumpkins.core.shared.application

import net.moviepumpkins.core.shared.error.BusinessException
import net.moviepumpkins.core.shared.logger
import java.util.ResourceBundle
import kotlin.reflect.KClass
import kotlin.reflect.KVisibility
import kotlin.reflect.full.memberProperties

class BusinessExceptionMessageResolver(
    private val messages: ResourceBundle
) {

    fun resolveOrNull(exception: BusinessException): String? {

        fun resolveMessageKey(exceptionClass: KClass<out BusinessException>): String {
            val simpleName = exceptionClass.simpleName
                ?: throw IllegalStateException("Business exception class has no simple name: ${exceptionClass.qualifiedName}")
            return simpleName.removeSuffix("Exception")
        }

        fun resolveMessageTemplate(key: String): String? = runCatching { messages.getString(key) }.getOrNull()

        val exceptionClass = exception.javaClass.kotlin
        val key = resolveMessageKey(exceptionClass)
        val template = resolveMessageTemplate(key)

        if (template == null) {
            logger().error("Missing template for exception: {}", exceptionClass.qualifiedName)
            return null
        }

        val publicProperties = exceptionClass.memberProperties
            .filter { it.visibility == KVisibility.PUBLIC }
            .associateBy { it.name }

        var allPropertiesFound = true

        val interpolated = PLACEHOLDER.replace(template) { match ->
            val propertyName = match.groupValues[1]
            val propertyValue = publicProperties[propertyName]
            if (propertyValue == null) {
                allPropertiesFound = false
                return@replace ""
            }
            propertyValue.getter.call(exception)?.toString() ?: "null"
        }

        return if (allPropertiesFound) {
            interpolated
        } else {
            logger().error("Malformatted error message template")
            null
        }
    }

    private companion object {
        val PLACEHOLDER = Regex("\\{([A-Za-z_][A-Za-z0-9_]*)}")
    }
}
