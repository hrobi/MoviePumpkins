package net.moviepumpkins.core.shared.service.exception

class ServiceException(val serviceError: ServiceError, val code: String, override val message: String) :
    RuntimeException()