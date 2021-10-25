package mashup.backend.spring.acm.domain.exception

import mashup.backend.spring.acm.domain.ResultCode

class MemberNotFoundException(
    override val message: String?,
    override val cause: Throwable? = null,
) : NotFoundException(
    resultCode = ResultCode.MEMBER_NOT_FOUND,
    message = message,
    cause = cause,
) {
    constructor(memberId: Long) : this(
        message = "회원정보가 없습니다. memberId: $memberId"
    )
}