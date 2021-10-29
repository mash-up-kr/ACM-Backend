package mashup.backend.spring.acm.domain.member

import mashup.backend.spring.acm.domain.perfume.Gender

data class MemberDetailVo(
    val id: Long,
    val status: MemberStatus,
    val name: String?,
    val gender: Gender?,
    val ageGroup: AgeGroup?,
) {
    constructor(member: Member) : this(
        id = member.id,
        status = member.memberStatus,
        name = member.memberDetail.name,
        gender = member.memberDetail.gender,
        ageGroup = member.memberDetail.ageGroup
    )
}
