package net.moviepumpkins.core.shared

class UnknownDiscriminatorException(val supposedDiscriminator: String, val sumClass: Class<*>) : RuntimeException()