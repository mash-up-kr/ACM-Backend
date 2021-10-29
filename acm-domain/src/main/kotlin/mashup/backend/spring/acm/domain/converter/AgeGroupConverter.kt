package mashup.backend.spring.acm.domain.converter

import mashup.backend.spring.acm.domain.member.AgeGroup
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter(autoApply = true)
class AgeGroupConverter: AttributeConverter<AgeGroup, String?> {
    override fun convertToDatabaseColumn(attribute: AgeGroup): String {
        return attribute.name
    }

    override fun convertToEntityAttribute(dbData: String?): AgeGroup {
        if (dbData == null) {
            return AgeGroup.UNKNOWN
        }
        return AgeGroup.valueOf(dbData)
    }
}