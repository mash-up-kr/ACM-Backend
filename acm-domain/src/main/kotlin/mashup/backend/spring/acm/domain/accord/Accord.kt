package mashup.backend.spring.acm.domain.accord

import mashup.backend.spring.acm.domain.BaseEntity
import javax.persistence.Entity

@Entity
class Accord(
    val name: String,
    val description: String
) : BaseEntity() {
}