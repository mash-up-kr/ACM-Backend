package mashup.backend.spring.acm.application.login

import mashup.backend.spring.acm.presentation.api.member.LoginRequest
import mashup.backend.spring.acm.presentation.api.member.LoginResponse
import mashup.backend.spring.acm.presentation.assembler.toMemberDetailResponse

fun LoginRequest.toVo(): LoginRequestVo = LoginRequestVo(
    idProviderType = this.idProviderType,
    idProviderUserId = this.idProviderUserId
)

fun LoginResponseVo.toLoginResponse(): LoginResponse {
    return LoginResponse(
        accessToken = this.accessToken,
        member = this.memberDetailVo.toMemberDetailResponse()
    )
}

