package mashup.backend.spring.acm.domain.perfume

class PerfumeNotFoundException(
    override val message: String? = null,
    override val cause: Throwable? = null
) : RuntimeException()