package mashup.backend.spring.acm.domain.exception

import mashup.backend.spring.acm.domain.ResultCode

class MemberInitializeFailedException(
    override val message: String? = null,
    override val cause: Throwable? = null,
) : BadRequestException(
    resultCode = ResultCode.MEMBER_STATUS_ALREADY_ACTIVE,
    message = message,
    cause = cause,
)