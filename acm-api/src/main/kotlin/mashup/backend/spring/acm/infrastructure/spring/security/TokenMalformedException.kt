package mashup.backend.spring.acm.infrastructure.spring.security

import org.springframework.security.core.userdetails.UsernameNotFoundException

class TokenMalformedException(
    override val message: String? = null,
    override val cause: Throwable? = null
) : UsernameNotFoundException(message, cause)