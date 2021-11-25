package mashup.backend.spring.acm.infrastructure.spring.mvc

import mashup.backend.spring.acm.domain.ResultCode
import mashup.backend.spring.acm.domain.exception.BadRequestException

class EnumConversionFailedException(
    override val message: String? = "올바르지 않은 enum 입니다.",
    override val cause: Throwable? = null,
): BadRequestException(
    resultCode = ResultCode.BAD_REQUEST,
    message = message,
    cause = cause
)