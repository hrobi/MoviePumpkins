package net.moviepumpkins.core.util

@JvmInline
value class StringMatcher(val string: String)

fun StringMatcher.isEmail(): Boolean = string.matches(Regex("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+\$"))
fun StringMatcher.isTrimmed(): Boolean = string.trim() == string
fun StringMatcher.isMultipleWords() = string.split(" ", limit = 2).size > 1

fun String.match(predicate: StringMatcher.() -> Boolean) = StringMatcher(this).predicate()