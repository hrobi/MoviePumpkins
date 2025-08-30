package net.moviepumpkins.core.app.map

import net.moviepumpkins.core.app.model.ErrorCode
import net.moviepumpkins.core.user.model.UserProfilePropertyConstraintViolation

fun UserProfilePropertyConstraintViolation.toErrorCode() = when (this) {
    UserProfilePropertyConstraintViolation.EMAIL_PATTERN -> ErrorCode("UUPCVEM01", "This is not valid e-mail address")
    UserProfilePropertyConstraintViolation.TRIMMED_DISPLAY_NAME -> ErrorCode(
        "UUPCVDN01",
        "Display name should not be surrounded by white spaces"
    )

    UserProfilePropertyConstraintViolation.TRIMMED_FULL_NAME -> ErrorCode(
        "UUPCVFN01",
        "Full name should not be surrounded by white spaces"
    )

    UserProfilePropertyConstraintViolation.FULLNAME_SHOULD_CONTAIN_AT_LEAST_TWO_WORDS -> ErrorCode(
        "UUPCVFN02",
        "Full name should contain at least two words"
    )
}