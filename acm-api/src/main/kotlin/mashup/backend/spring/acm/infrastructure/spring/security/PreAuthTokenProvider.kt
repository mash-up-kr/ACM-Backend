package mashup.backend.spring.acm.infrastructure.spring.security

import mashup.backend.spring.acm.application.TokenService
import mashup.backend.spring.acm.domain.member.MemberService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken


class PreAuthTokenProvider : AuthenticationProvider {
    @Autowired
    lateinit var memberService: MemberService

    @Autowired
    lateinit var jwtService: TokenService<Long>

    @Throws(AuthenticationException::class)
    override fun authenticate(authentication: Authentication): Authentication {
        log.debug("authentication: ${SecurityContextHolder.getContext().authentication}")
        if (authentication is PreAuthenticatedAuthenticationToken) {
            val token = authentication.getPrincipal() as String
            val member = jwtService.decode(token)
                ?.let { memberService.findById(it) }
                ?: throw TokenMalformedException("Invalid token")
            return UsernamePasswordAuthenticationToken(
                member.id.toString(),
                "",
                listOf(SimpleGrantedAuthority(ROLE_MEMBER))
            )
        }
        throw TokenMissingException("Invalid token")
    }

    override fun supports(authentication: Class<*>): Boolean {
        return PreAuthenticatedAuthenticationToken::class.java.isAssignableFrom(authentication)
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(this::class.java)
        const val ROLE_MEMBER = "MEMBER"
    }
}