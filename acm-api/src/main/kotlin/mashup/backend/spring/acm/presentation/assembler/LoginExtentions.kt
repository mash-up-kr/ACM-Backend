package mashup.backend.spring.acm.presentation.assembler

import mashup.backend.spring.acm.application.login.LoginRequestVo
import mashup.backend.spring.acm.application.login.LoginResponseVo
import mashup.backend.spring.acm.presentation.api.member.LoginRequest
import mashup.backend.spring.acm.presentation.api.member.LoginResponse
import java.lang.IllegalArgumentException

fun LoginRequest.toVo(): LoginRequestVo = LoginRequestVo(
    idProviderType = this.idProviderType ?: throw IllegalArgumentException("'idProviderType' must not be null"),
    idProviderUserId = this.idProviderUserId ?: throw IllegalArgumentException("'idProviderUserId' must not be null")
)

fun LoginResponseVo.toLoginResponse(): LoginResponse {
    return LoginResponse(
        accessToken = this.accessToken,
        member = this.memberDetailVo.toMemberDetailResponse()
    )
}

