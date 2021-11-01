package mashup.backend.spring.acm.domain.member

class SimpleMemberDetailVo (
    val gender: MemberGender?,
    val ageGroup: AgeGroup?,
    val noteGroupIds: List<Long>
) {
    constructor(memberDetail: MemberDetail) : this(
        gender = memberDetail.gender,
        ageGroup = memberDetail.ageGroup,
        noteGroupIds = memberDetail.noteGroupIds
    )
}