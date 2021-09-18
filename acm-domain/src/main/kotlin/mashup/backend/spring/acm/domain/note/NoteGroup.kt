package mashup.backend.spring.acm.domain.note

import mashup.backend.spring.acm.domain.BaseEntity
import javax.persistence.Entity

@Entity
class NoteGroup(
    val name: String,
    val description: String
) : BaseEntity() {
}