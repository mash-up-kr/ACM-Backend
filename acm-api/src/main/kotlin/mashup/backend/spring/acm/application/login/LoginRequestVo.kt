package mashup.backend.spring.acm.application.login

import mashup.backend.spring.acm.domain.member.idprovider.IdProviderType

data class LoginRequestVo(
    val idProviderType: IdProviderType,
    val idProviderUserId: String
)
