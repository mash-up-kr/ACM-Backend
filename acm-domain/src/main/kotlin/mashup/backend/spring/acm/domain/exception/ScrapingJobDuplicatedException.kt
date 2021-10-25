package mashup.backend.spring.acm.domain.exception

import mashup.backend.spring.acm.domain.ResultCode

class ScrapingJobDuplicatedException(
    override val message: String? = null,
    override val cause: Throwable? = null,
) : BadRequestException(
    resultCode = ResultCode.SCRAPING_JOB_ALREADY_EXIST,
    message = message,
    cause = cause,
)