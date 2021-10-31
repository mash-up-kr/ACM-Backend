package mashup.backend.spring.acm.domain.member

data class MemberDetailVo(
    val id: Long,
    val status: MemberStatus,
    val name: String?,
    val gender: MemberGender?,
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
