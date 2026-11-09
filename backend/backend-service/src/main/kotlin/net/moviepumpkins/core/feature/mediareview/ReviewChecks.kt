package net.moviepumpkins.core.feature.mediareview

import net.moviepumpkins.core.shared.validation.Check
import net.moviepumpkins.core.shared.validation.lengthBetween
import kotlin.reflect.KProperty1

object ReviewChecks {

    val titleCheck = Check {
        lengthBetween(3, 50)
    }

    val contentCheck = Check {
        lengthBetween(20, 1000)
    }

    fun <T> checkAll(
        title: KProperty1<T, String>,
        content: KProperty1<T, String>,
    ) = Check {
        title by titleCheck
        content by contentCheck
    }
}