package mashup.backend.spring.acm.domain.member

import mashup.backend.spring.acm.domain.BaseEntity
import java.time.LocalDate
import java.time.Year
import javax.persistence.Entity

@Entity
class MemberDetail(
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
    @Transient
    var age: Int = 0
        set(value) {
            birthYear = calculateBirthYear(value)
            field = value
        }
        get() = calculateAge(birthYear)

    private fun calculateBirthYear(age: Int): Year {
        return Year.of(LocalDate.now().year - age + 1)
    }

    private fun calculateAge(birthYear: Year): Int {
        return LocalDate.now().year - birthYear.value + 1
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MemberDetail

        if (gender != other.gender) return false
        if (birthYear != other.birthYear) return false
        if (note != other.note) return false
        if (perfume != other.perfume) return false

        return true
    }

    override fun hashCode(): Int {
        var result = gender.hashCode()
        result = 31 * result + birthYear.hashCode()
        result = 31 * result + note.hashCode()
        result = 31 * result + perfume.hashCode()
        return result
    }

    override fun toString(): String {
        return "MemberDetail(gender='$gender', birthYear=$birthYear, note='$note', perfume='$perfume')"
    }

    init {
        this.age = calculateAge(birthYear)
    }
}