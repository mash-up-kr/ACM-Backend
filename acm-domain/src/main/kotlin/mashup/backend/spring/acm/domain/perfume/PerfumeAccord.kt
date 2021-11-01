package mashup.backend.spring.acm.domain.perfume

import mashup.backend.spring.acm.domain.BaseEntity
import mashup.backend.spring.acm.domain.accord.Accord
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
class PerfumeAccord(
    /**
     * 향수에서 이 accord 가 어느정도의 비중을 차지하는지 (웹페이지에서는 width [0:100] 값 으로 표현함)
     */
    val width: Double?,

    val opacity: Double?,

    @ManyToOne
    @JoinColumn
    val perfume: Perfume,

    @ManyToOne
    val accord: Accord,
) : BaseEntity() {
}
