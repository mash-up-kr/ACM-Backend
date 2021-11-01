package mashup.backend.spring.acm.domain.accord

import mashup.backend.spring.acm.domain.BaseEntity
import javax.persistence.Entity

@Entity
class Accord(
    val name: String,
    val textColor: String,
    val backgroundColor: String,
) : BaseEntity() {
    constructor(accordCreateVo: AccordCreateVo) : this(
        name = accordCreateVo.name,
        textColor = accordCreateVo.textColor,
        backgroundColor = accordCreateVo.backgroundColor,
    )
}
