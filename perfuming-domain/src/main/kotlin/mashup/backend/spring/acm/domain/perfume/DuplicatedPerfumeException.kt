package mashup.backend.spring.acm.domain.perfume

import java.lang.RuntimeException

class DuplicatedPerfumeException(
    override val message: String? = null,
    override val cause: Throwable? = null
) : RuntimeException()