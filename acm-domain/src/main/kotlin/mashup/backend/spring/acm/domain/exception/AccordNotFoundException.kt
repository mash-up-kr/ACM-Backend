package mashup.backend.spring.acm.domain.exception

import mashup.backend.spring.acm.domain.ResultCode

class AccordNotFoundException(
    override val message: String? = null,
    override val cause: Throwable? = null,
) : NotFoundException(
    resultCode = ResultCode.ACCORD_NOT_FOUND,
    message = message,
    cause = cause,
) {
    constructor(accordId: Long) : this(
        message = "어코드를 찾을 수 없습니다. accordId: $accordId",
    )
}

