package net.moviepumpkins.core.user.model

enum class UserProfileUpdateForbiddenError {
    CANNOT_MODIFY_OTHERS_PROFILE
}

enum class UserProfilePropertyConstraintViolation {
    EMAIL_PATTERN,
    TRIMMED_DISPLAY_NAME,
    TRIMMED_FULL_NAME,
    FULLNAME_SHOULD_CONTAIN_AT_LEAST_TWO_WORDS
}

data class UserProfileUpdate(
    val fullName: String,
    val displayName: String,
    val email: String
)

data class UserProfile(
    val username: String,
    val fullName: String,
    val email: String,
    val displayName: String,
    val role: UserRole,
)