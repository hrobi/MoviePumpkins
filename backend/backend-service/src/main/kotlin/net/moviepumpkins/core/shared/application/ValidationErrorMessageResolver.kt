package net.moviepumpkins.core.shared.application

import net.moviepumpkins.core.shared.validation.Constraint
import java.util.ResourceBundle

class ValidationErrorMessageResolver(
    private val messages: ResourceBundle
) {

    fun resolveOrNull(constraint: Constraint): String? {
        val kclass = constraint::class
        return MessageByClassResolver.resolveOrNull(
            messages,
            kclass.simpleName?.removeSuffix("Constraint")
                ?: throw IllegalStateException("Constraint has no simple name"),
            constraint,
            constraint::class
        )
    }

}