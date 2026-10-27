package net.moviepumpkins.core.shared.application

import net.moviepumpkins.core.shared.logger
import java.util.ResourceBundle
import kotlin.reflect.KClass
import kotlin.reflect.KVisibility
import kotlin.reflect.full.memberProperties

object MessageByClassResolver {

    val PLACEHOLDER = Regex("\\{([A-Za-z_][A-Za-z0-9_]*)}")

    fun <T : Any> resolveOrNull(messages: ResourceBundle, key: String, obj: T, clazz: KClass<out T>): String? {

        val template = runCatching { messages.getString(key) }.getOrNull()

        if (template == null) {
            logger().error("Missing template for key: {}", key)
            return null
        }

        val publicProperties = clazz.memberProperties
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
            propertyValue.getter.call(obj)?.toString() ?: "null"
        }

        return if (allPropertiesFound) {
            interpolated
        } else {
            logger().error("Malformatted error message template")
            null
        }
    }

}