package mashup.backend.spring.acm.domain.perfume

import mashup.backend.spring.acm.domain.member.MemberGender

enum class Gender {
    WOMAN,
    MAN,
    UNISEX,
    UNKNOWN;

    fun getMemberGender(): MemberGender {
        return when (this) {
            WOMAN -> MemberGender.FEMALE
            MAN -> MemberGender.MALE
            UNISEX, UNKNOWN ->  MemberGender.UNKNOWN
        }
    }
}