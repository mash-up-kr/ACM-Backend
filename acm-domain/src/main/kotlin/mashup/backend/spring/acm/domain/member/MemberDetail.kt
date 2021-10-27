package mashup.backend.spring.acm.domain.member

import mashup.backend.spring.acm.domain.BaseEntity
import mashup.backend.spring.acm.domain.converter.NumberListAndStringConverter
import mashup.backend.spring.acm.domain.perfume.Gender
import java.time.Year
import javax.persistence.*

@Entity
class MemberDetail(
    /**
     * 이름
     */
    var name: String,
    /**
     * 성별
     */
    @Enumerated(EnumType.STRING)
    var gender: Gender,
    /**
     * 출생연도
     */
    @Embedded
    var age: Age,
    /**
     * 좋아하는 노트
     */
    @Convert(converter = NumberListAndStringConverter::class)
    var noteGroupIds: List<Long>,
    /**
     * 좋아하는 향수
     */
    @Convert(converter = NumberListAndStringConverter::class)
    var perfumeIds: List<Long>,
) : BaseEntity() {

    companion object {
        fun empty(): MemberDetail {
            return MemberDetail(
                name = "",
                gender = Gender.UNKNOWN,
                age = Age.UNKNOWN,
                noteGroupIds = emptyList(),
                perfumeIds = emptyList(),
            )
        }
    }

    fun initialize(requestVo: MemberInitializeRequestVo) {
        requestVo.name?.run { name = this }
        requestVo.gender?.run { gender = this }
        requestVo.age?.run { age = Age(this, Year.now()) }
        requestVo.noteGroupIds?.run { noteGroupIds = this }
        requestVo.perfumeIds?.run { perfumeIds = this }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MemberDetail

        if (gender != other.gender) return false
        if (age != other.age) return false

        return true
    }

    override fun hashCode(): Int {
        var result = gender.hashCode()
        result = 31 * result + age.hashCode()
        return result
    }

    override fun toString(): String {
        return "MemberDetail(gender='$gender', age=$age')"
    }
}