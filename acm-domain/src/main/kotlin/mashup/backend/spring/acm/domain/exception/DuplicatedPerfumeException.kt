package mashup.backend.spring.acm.domain.exception

import java.lang.RuntimeException

class DuplicatedPerfumeException(
    override val message: String? = null,
    override val cause: Throwable? = null
) : RuntimeException()