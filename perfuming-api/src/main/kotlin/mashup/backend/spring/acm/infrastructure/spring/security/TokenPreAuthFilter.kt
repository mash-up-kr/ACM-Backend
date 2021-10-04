package mashup.backend.spring.acm.infrastructure.spring.security

import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter
import org.springframework.util.StringUtils
import java.util.regex.Pattern
import javax.servlet.http.HttpServletRequest

class TokenPreAuthFilter : AbstractPreAuthenticatedProcessingFilter() {
    override fun getPreAuthenticatedPrincipal(request: HttpServletRequest): Any? {
        return resolveToken(request)
    }

    override fun getPreAuthenticatedCredentials(request: HttpServletRequest): Any? {
        return resolveToken(request)
    }

    private fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")
        if (!StringUtils.hasText(bearerToken)) {
            return null
        }
        val matcher = BEARER_TOKEN_PATTERN.matcher(bearerToken)
        return if (!matcher.matches()) {
            null
        } else matcher.group(1)
    }

    companion object {
        private val BEARER_TOKEN_PATTERN = Pattern.compile("Bearer (.*)")
    }
}
