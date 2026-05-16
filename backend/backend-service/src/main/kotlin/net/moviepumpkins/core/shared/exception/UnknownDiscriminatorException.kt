package net.moviepumpkins.core.shared.exception

class UnknownDiscriminatorException(val supposedDiscriminator: String, val sumClass: Class<*>) : RuntimeException()