package mashup.backend.spring.acm.presentation

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.security.Principal

@RestControllerAdvice
class ApiControllerAdvice {
    @ModelAttribute("memberId")
    fun resolveMemberId(principal: Principal?): Long? {
        log.debug("principal : $principal")
        if (principal is UsernamePasswordAuthenticationToken) {
            return principal.principal.toString().toLong()
        }
        return null
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(ApiControllerAdvice::class.java)
    }
}