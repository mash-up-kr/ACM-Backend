package mashup.backend.spring.acm.domain.recommend

import mashup.backend.spring.acm.domain.member.MemberDetailVo

data class RecommendRequestVo(
    val memberDetailVo: MemberDetailVo?,
    val size: Int,
    val exceptIds: Set<Long>?,
)