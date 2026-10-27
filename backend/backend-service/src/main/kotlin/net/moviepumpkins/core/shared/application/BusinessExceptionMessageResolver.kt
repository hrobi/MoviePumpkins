package net.moviepumpkins.core.shared.application

import net.moviepumpkins.core.shared.error.BusinessException
import java.util.ResourceBundle
import kotlin.reflect.KClass

class BusinessExceptionMessageResolver(
    private val messages: ResourceBundle
) {

    fun resolveOrNull(exception: BusinessException): String? {

        fun resolveMessageKey(exceptionClass: KClass<out BusinessException>): String {
            val simpleName = exceptionClass.simpleName
                ?: throw IllegalStateException("Business exception class has no simple name: ${exceptionClass.qualifiedName}")
            return simpleName.removeSuffix("Exception")
        }

        return MessageByClassResolver.resolveOrNull(
            messages,
            resolveMessageKey(exception::class),
            exception,
            exception::class
        )
    }

}
