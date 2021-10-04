package mashup.backend.spring.acm.infrastructure.spring.security

import mashup.backend.spring.acm.application.TokenService
import mashup.backend.spring.acm.domain.member.MemberService
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

    @Autowired
    lateinit var jwtService: TokenService<Long>

    @Throws(AuthenticationException::class)
    override fun authenticate(authentication: Authentication): Authentication {
        if (authentication is PreAuthenticatedAuthenticationToken) {
            val token = authentication.getPrincipal() as String
            val member = jwtService.decode(token)
                ?.let { memberService.findById(it) }
                ?: throw TokenMalformedException("Invalid token")
            return UsernamePasswordAuthenticationToken(
                member.id.toString(),
                member.id.toString(), // TODO: credential
                listOf(SimpleGrantedAuthority("USER"))
            )
        }
        throw TokenMissingException("Invalid token")
    }

    override fun supports(authentication: Class<*>): Boolean {
        return PreAuthenticatedAuthenticationToken::class.java.isAssignableFrom(authentication)
    }
}