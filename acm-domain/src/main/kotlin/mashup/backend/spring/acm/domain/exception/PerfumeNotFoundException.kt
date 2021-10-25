package mashup.backend.spring.acm.domain.exception

import mashup.backend.spring.acm.domain.ResultCode

class PerfumeNotFoundException(
    override val message: String? = null,
    override val cause: Throwable? = null,
) : NotFoundException(
    resultCode = ResultCode.PERFUME_NOT_FOUND,
    message = message,
    cause = cause
)