package mashup.backend.spring.acm.domain.converter

import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter
class NumberListAndStringConverter : AttributeConverter<List<Long>, String> {
    override fun convertToDatabaseColumn(attribute: List<Long>?): String {
        if (attribute == null) {
            return ""
        }
        return attribute.joinToString { it.toString() }
    }

    override fun convertToEntityAttribute(dbData: String?): List<Long> {
        if (dbData == null) {
            return ArrayList()
        }
        if (dbData.isBlank()) {
            return ArrayList()
        }
        return dbData.split(",")
            .map { it.trim() }
            .map { it.toLong() }
    }
}