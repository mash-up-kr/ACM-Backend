package mashup.backend.spring.acm.application.login

import mashup.backend.spring.acm.domain.member.MemberDetailVo

data class LoginResponseVo(
    val accessToken: String,
    val memberDetailVo: MemberDetailVo
)
