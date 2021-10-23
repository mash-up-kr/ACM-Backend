package mashup.backend.spring.acm.domain.member

import java.time.Year
import javax.persistence.Column
import javax.persistence.Embeddable

/**
 * 생년월일 기반으로 나이를 관리함
 */
@Embeddable
class Age(
    @Column
    private val birthYear: Year,
) {
    @Transient
    val value: Int = Year.now().value - birthYear.value + 1

    constructor(age: Int, currentYear: Year) : this(
        birthYear = Year.of(currentYear.value - age + 1)
    )

    companion object {
        val UNKNOWN: Age = Age(birthYear = Year.of(Year.MIN_VALUE))
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Age

        if (birthYear != other.birthYear) return false

        return true
    }

    override fun hashCode(): Int = birthYear.hashCode()

}