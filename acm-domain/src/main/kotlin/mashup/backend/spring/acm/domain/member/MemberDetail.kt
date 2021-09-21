package mashup.backend.spring.acm.domain.member

import mashup.backend.spring.acm.domain.BaseEntity
import java.time.LocalDate
import java.time.Year
import javax.persistence.Entity
import javax.persistence.OneToOne

@Entity
class MemberDetail(
    @OneToOne
    val member: Member,
    /**
     * 성별
     */
    var gender: String,
    /**
     * 출생연도
     */
    private var birthYear: Year,
    /**
     * 좋아하는 노트
     */
    var note: String = "",
    /**
     * 좋아하는 향수
     */
    var perfume: String = "",
) : BaseEntity() {
    /**
     * 나이
     */
    var age: Int = 0
        set(value) {
            birthYear = Year.of(LocalDate.now().year - value + 1)
            field = value
        }
        get() = LocalDate.now().year - birthYear.value + 1
}