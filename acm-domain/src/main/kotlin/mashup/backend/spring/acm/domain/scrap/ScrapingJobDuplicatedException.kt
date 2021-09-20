package mashup.backend.spring.acm.domain.scrap

class ScrapingJobDuplicatedException(
    override val message: String? = null,
    override val cause: Throwable? = null
) : RuntimeException(message, cause)