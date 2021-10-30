package mashup.backend.spring.acm.domain.exception

import mashup.backend.spring.acm.domain.ResultCode

class MemberNicknameInvalidException(
    override val message: String?,
    override val cause: Throwable?,
) : BadRequestException(
    resultCode = ResultCode.MEMBER_NICKNAME_INVALID,
    message = message,
    cause = cause,
) {
    constructor(nickname: String): this(
        message = "${ResultCode.MEMBER_NICKNAME_INVALID.message}. nickname:$nickname",
        cause = null,
    )
}