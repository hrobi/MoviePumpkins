package net.moviepumpkins.core.shared.validation

import kotlin.reflect.KProperty1

interface Constraint

sealed interface CheckResult<out T> {

    fun <K> map(transform: (T) -> K): CheckResult<K> {
        return when (this) {
            is Invalid -> this
            is Valid -> Valid(transform(value))
        }
    }

}

data class Invalid(val errors: List<ValidationError>) : CheckResult<Nothing>
data class Valid<T>(val value: T) : CheckResult<T>

data class ValidationError(val path: String?, val value: Any?, val violations: List<Constraint>) {

    fun prefixed(prefix: String) = copy(path = "$prefix${if (path != null) ".$path" else ""}")

}

class Checking<T>(val value: T) {

    private val fieldErrors = mutableListOf<ValidationError>()
    private val valueErrors = mutableListOf<Constraint>()

    val errors: List<ValidationError>
        get() = buildList {
            if (valueErrors.isNotEmpty()) {
                add(ValidationError(path = null, value, valueErrors))
            }
            addAll(fieldErrors)
        }

    fun raise(constraint: Constraint) {
        valueErrors.add(constraint)
    }

    fun raise(validationError: ValidationError) {
        fieldErrors.add(validationError)
    }

    fun run(check: Check<T>) {
        check.validationAction(this)
    }

    fun raiseAll(errors: Collection<ValidationError>) {
        for (error in errors) {
            raise(error)
        }
    }

    inline operator fun <K> KProperty1<T, K>.invoke(checkingAction: Checking<K>.() -> Unit) {
        val propertyValue = get(value)
        val checking = Checking(propertyValue)
        checking.checkingAction()

        raiseAll(checking.errors.map { it.prefixed(name) })
    }

    inline fun <K> KProperty1<T, K>.asValue(otherwise: Constraint, checkingAction: Checking<K>.() -> Unit) {
        asValue({ listOf(otherwise) }, checkingAction)
    }

    inline fun <K> KProperty1<T, K>.asValue(otherwise: () -> List<Constraint>, checkingAction: Checking<K>.() -> Unit) {
        val propertyValue = get(value)
        val checking = Checking(propertyValue)
        checking.checkingAction()
        raise(ValidationError(path = name, propertyValue, otherwise()))
    }

    infix fun <K> KProperty1<T, K>.by(check: Check<K>) {
        val propertyValue = get(value)
        val result = check.validate(propertyValue)
        if (result is Invalid) {
            raiseAll(result.errors.map { it.prefixed(name) })
        }
    }

    inline fun <K> KProperty1<T, Collection<K>>.each(checkingAction: Checking<K>.() -> Unit) {
        val propertyValue = get(value)
        propertyValue.forEachIndexed { index, item ->

            val itemChecking = Checking(item)
            itemChecking.checkingAction()

            raiseAll(itemChecking.errors.map { it.prefixed("$name[$index]") })
        }
    }

    inline fun <K> KProperty1<T, Collection<K>>.eachAsValue(
        otherwise: Constraint,
        checkingAction: Checking<K>.() -> Unit
    ) {
        eachAsValue({ listOf(otherwise) }, checkingAction)
    }

    inline fun <K> KProperty1<T, Collection<K>>.eachAsValue(
        otherwise: () -> List<Constraint>,
        checkingAction: Checking<K>.() -> Unit
    ) {
        val propertyValue = get(value)
        propertyValue.forEachIndexed { index, item ->

            val itemChecking = Checking(item)
            itemChecking.checkingAction()

            raise(ValidationError(path = "$name[$index]", item, otherwise()))
        }
    }

}

class Check<T>(internal val validationAction: Checking<T>.() -> Unit) {

    fun validate(value: T): CheckResult<T> {
        val checking = Checking(value)
        checking.validationAction()
        return if (checking.errors.isNotEmpty()) {
            Invalid(checking.errors)
        } else {
            Valid(value)
        }
    }

}

fun <T> runCheck(value: T, validationAction: Checking<T>.() -> Unit) = Check(validationAction).validate(value)