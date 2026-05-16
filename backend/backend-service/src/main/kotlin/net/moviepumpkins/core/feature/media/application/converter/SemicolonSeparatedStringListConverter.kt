package net.moviepumpkins.core.feature.media.application.converter

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class SemicolonSeparatedStringListConverter : AttributeConverter<List<String>, String> {

    override fun convertToDatabaseColumn(list: List<String>?): String? = list?.joinToString(";")

    override fun convertToEntityAttribute(str: String?): List<String>? = str?.split(";")

}