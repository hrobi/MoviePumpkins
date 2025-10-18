package net.moviepumpkins.core.util.validations

import io.konform.validation.ValidationBuilder
import io.konform.validation.constraints.pattern

fun ValidationBuilder<String>.email() =
    pattern(Regex("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+\$")) hint "should be an e-mail"

fun ValidationBuilder<String>.trimmed() = constrain("should be trimmed") { it.trim() == it }

fun ValidationBuilder<String>.multipleWords() =
    constrain("should contain at least 2 words") { it.split(" ", limit = 2).size > 1 }