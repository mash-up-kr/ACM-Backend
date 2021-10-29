package mashup.backend.spring.acm.domain.member

import mashup.backend.spring.acm.domain.perfume.Gender

data class MemberInitializeRequestVo(
    val gender: Gender,
    val ageGroup: AgeGroup,
    val noteGroupIds: List<Long>?,
)
