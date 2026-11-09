package net.moviepumpkins.core.shared.validation

object NotEmptyConstraint : Constraint
data class LengthBetweenConstraint(val min: Int, val max: Int) : Constraint

fun Checking<String>.notEmpty() {
    if (value.isEmpty()) {
        raise(NotEmptyConstraint)
    }
}

fun Checking<String>.lengthBetween(min: Int, max: Int) {
    if (value.length !in (min..max)) {
        raise(LengthBetweenConstraint(min, max))
    }
}