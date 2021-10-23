package mashup.backend.spring.acm.domain.member

import mashup.backend.spring.acm.BusinessException
import mashup.backend.spring.acm.domain.ResultCode

class MemberInitializeFailedException(
    override val message: String? = null,
) : BusinessException(
    resultCode = ResultCode.MEMBER_STATUS_ALREADY_ACTIVE,
    message = message
)