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
     * 나이대 (10대, 20대, ...)
     */
    var ageGroup: AgeGroup,
    /**
     * 좋아하는 노트
     */
    @Convert(converter = NumberListAndStringConverter::class)
    var noteGroupIds: List<Long>,
) : BaseEntity() {

    companion object {
        fun empty(): MemberDetail {
            return MemberDetail(
                name = "",
                gender = Gender.UNKNOWN,
                ageGroup = AgeGroup.UNKNOWN,
                noteGroupIds = emptyList(),
            )
        }
    }

    fun initialize(requestVo: MemberInitializeRequestVo) {
        requestVo.gender.run { gender = this }
        requestVo.ageGroup.run { ageGroup = this }
        requestVo.noteGroupIds?.run { noteGroupIds = this }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MemberDetail

        if (name != other.name) return false
        if (gender != other.gender) return false
        if (ageGroup != other.ageGroup) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + gender.hashCode()
        result = 31 * result + ageGroup.hashCode()
        return result
    }

    override fun toString(): String {
        return "MemberDetail(name='$name', gender=$gender, ageGroup=$ageGroup, noteGroupIds=$noteGroupIds)"
    }
}