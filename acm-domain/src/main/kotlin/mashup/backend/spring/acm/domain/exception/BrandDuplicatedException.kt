package mashup.backend.spring.acm.domain.exception

import mashup.backend.spring.acm.domain.ResultCode

class BrandDuplicatedException(
    override val message: String? = null,
    override val cause: Throwable? = null,
) : BusinessException(
    resultCode = ResultCode.BRAND_ALREADY_EXIST,
    message = message,
    cause = cause
)
