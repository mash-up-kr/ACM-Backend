package mashup.backend.spring.acm.domain.member

data class MemberInitializeRequestVo(
    val gender: MemberGender,
    val ageGroup: AgeGroup,
    val noteGroupIds: List<Long>?,
)
