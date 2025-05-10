package net.moviepumpkins.core.user

fun SimpleUserView.toUserAccount() = UserAccount(
    username = username,
    email = email,
    fullName = fullName,
    role = role,
)

fun UserAccount.toUserAccountEntity() = UserAccountEntity(
    username = username,
    email = email,
    fullName = fullName,
    about = "",
    role = role,
)