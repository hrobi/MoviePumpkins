package net.moviepumpkins.core.util

import kotlin.reflect.KProperty0

class CheckThat<out V>(val property: KProperty0<V>, val propertyPathPrefix: String = "")

data class PropertyCheck(val propertyPaths: String, val matches: Boolean, val errorMessageIfNotMatching: String)

fun <V> checkThat(property: KProperty0<V>, propertyPathPrefix: String = "") = CheckThat(property, propertyPathPrefix)

class PropertyMatch<out V>(val property: KProperty0<V>, val matches: Boolean, val propertyPathPrefix: String = "")

infix fun <V> PropertyMatch<V>.orElse(message: String) =
    PropertyCheck(propertyPathPrefix + property.name, matches, message)

infix fun CheckThat<String>.matches(predicate: StringMatcher.() -> Boolean) =
    PropertyMatch(property, property.get().match(predicate), propertyPathPrefix = propertyPathPrefix)

fun checkEmail(property: KProperty0<String>, propertyPathPrefix: String = "") =
    checkThat(property, propertyPathPrefix) matches { isEmail() } orElse "should be valid email"

fun checkTrimmed(property: KProperty0<String>, propertyPathPrefix: String = "") =
    checkThat(property, propertyPathPrefix) matches { isTrimmed() } orElse "should be trimmed"

fun checkIsMultipleWords(property: KProperty0<String>, propertyPathPrefix: String = "") =
    checkThat(property, propertyPathPrefix) matches { isMultipleWords() } orElse "should consist of at least two words"

fun PropertyCheck.require() {
    if (!matches) {
        throw IllegalArgumentException("$propertyPaths $errorMessageIfNotMatching")
    }
}