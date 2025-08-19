package net.moviepumpkins.core.application

enum class ErrorCode(val message: String) {
    USER_PROPERTY_CONSTRAINT_01("E-mail is invalid"),
    USER_PROPERTY_CONSTRAINT_02("Display name should not be surrounded by whitespaces"),
    USER_PROPERTY_CONSTRAINT_03("Full name should not be surrounded by whitespaces"),
    USER_PROPERTY_CONSTRAINT_04("Full name should be composed of at least 2 name parts which should only have one space between them"),
}