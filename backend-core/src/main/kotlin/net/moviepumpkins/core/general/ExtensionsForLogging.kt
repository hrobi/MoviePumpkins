package net.moviepumpkins.core.general

import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun Any.getLogger(): Logger = LoggerFactory.getLogger(javaClass)