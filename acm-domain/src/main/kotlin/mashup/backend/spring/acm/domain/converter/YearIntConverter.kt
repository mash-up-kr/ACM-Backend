package mashup.backend.spring.acm.domain.converter

import java.time.Year
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter(autoApply = true)
class YearIntConverter: AttributeConverter<Year, Int> {
    override fun convertToDatabaseColumn(attribute: Year): Int = attribute.value

    override fun convertToEntityAttribute(dbData: Int): Year = Year.of(dbData)
}