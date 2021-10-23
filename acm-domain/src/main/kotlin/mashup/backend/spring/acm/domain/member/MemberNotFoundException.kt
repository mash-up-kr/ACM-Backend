package mashup.backend.spring.acm.domain.member

import mashup.backend.spring.acm.BusinessException
import mashup.backend.spring.acm.domain.ResultCode

class MemberNotFoundException(
    override val message: String?,
) : BusinessException(resultCode = ResultCode.MEMBER_NOT_FOUND, message = message) {
    constructor(memberId: Long): this(
        message = "회원정보가 없습니다. memberId: $memberId"
    )
}