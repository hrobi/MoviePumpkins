package net.moviepumpkins.core.userreport.exception

class UserReportStateException(val state: UserReportStateError) : RuntimeException()

sealed interface UserReportStateError {
    data object ReportedUserDoesNotExist : UserReportStateError
}