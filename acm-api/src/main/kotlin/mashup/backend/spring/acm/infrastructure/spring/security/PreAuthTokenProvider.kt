package mashup.backend.spring.acm.infrastructure.spring.security

import mashup.backend.spring.acm.domain.member.Member
import mashup.backend.spring.acm.domain.member.MemberService
import mashup.backend.spring.acm.domain.member.idprovider.IdProviderInfo
import mashup.backend.spring.acm.domain.member.idprovider.IdProviderType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken


class PreAuthTokenProvider : AuthenticationProvider {
    @Autowired
    lateinit var memberService: MemberService

    @Throws(AuthenticationException::class)
    override fun authenticate(authentication: Authentication): Authentication {
        if (authentication is PreAuthenticatedAuthenticationToken) {
            val token = authentication.getPrincipal() as String
            // TODO: token -> idProviderInfo
            val idProviderInfo = getIdProviderInfo(token)
            val member: Member = memberService.findByIdProviderVo(idProviderInfo)
                ?: throw TokenMalformedException("Invalid token")
            return UsernamePasswordAuthenticationToken(
                member.id.toString(),
                member.id.toString(),
                listOf(SimpleGrantedAuthority("USER"))
            )
        }
        throw TokenMissingException("Invalid token")
    }

    private fun getIdProviderInfo(token: String): IdProviderInfo = IdProviderInfo(
        idProviderType = IdProviderType.UUID,
        idProviderUserId = "idProviderUserId"
    )

    override fun supports(authentication: Class<*>?): Boolean {
        return PreAuthenticatedAuthenticationToken::class.java.isAssignableFrom(authentication)
    }
}