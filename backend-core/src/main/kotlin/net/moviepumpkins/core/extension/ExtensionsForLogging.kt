package net.moviepumpkins.core.extension

import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun Any.getLogger(): Logger = LoggerFactory.getLogger(javaClass)