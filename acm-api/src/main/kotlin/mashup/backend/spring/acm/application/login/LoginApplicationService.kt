package mashup.backend.spring.acm.application

import mashup.backend.spring.acm.application.login.LoginRequestVo
import mashup.backend.spring.acm.application.login.LoginResponseVo
import mashup.backend.spring.acm.domain.member.MemberService
import mashup.backend.spring.acm.domain.member.idprovider.IdProviderInfo
import org.springframework.stereotype.Service

interface LoginApplicationService {
    fun login(loginRequestVo: LoginRequestVo): LoginResponseVo
}

@Service
class LoginApplicationServiceImpl(
    private val memberService: MemberService,
    private val jwtService: TokenService<Long>,
) : LoginApplicationService {

    override fun login(loginRequestVo: LoginRequestVo): LoginResponseVo {
        val memberDetailVo = memberService.join(
            idProviderInfo = IdProviderInfo(
                idProviderType = loginRequestVo.idProviderType,
                idProviderUserId = loginRequestVo.idProviderUserId
            )
        )
        return LoginResponseVo(
            accessToken = jwtService.encode(memberDetailVo.id),
            memberDetailVo = memberDetailVo
        )
    }

}