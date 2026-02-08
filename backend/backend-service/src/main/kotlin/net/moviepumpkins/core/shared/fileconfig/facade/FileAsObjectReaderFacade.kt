package net.moviepumpkins.core.shared.fileconfig.facade

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import java.io.File

@Component
class FileAsObjectReaderFacade(private val objectMapper: ObjectMapper) {
    fun <V> readUTF8JsonFile(file: File, clazz: Class<V>): V {
        return objectMapper.readValue(file, clazz)
    }

    final inline fun <reified V> readUTF8JsonFile(file: File): V {
        return readUTF8JsonFile(file, V::class.java)
    }
}