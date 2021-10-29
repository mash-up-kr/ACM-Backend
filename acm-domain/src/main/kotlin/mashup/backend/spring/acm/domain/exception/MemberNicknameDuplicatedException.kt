package mashup.backend.spring.acm.domain.exception

import mashup.backend.spring.acm.domain.ResultCode

class MemberNicknameDuplicatedException(
    override val message: String?,
    override val cause: Throwable?,
): BusinessException(
    resultCode = ResultCode.MEMBER_NICKNAME_ALREADY_EXIST,
    message = message,
    cause = cause
) {
    constructor(nickname: String) : this(
        message = "${ResultCode.MEMBER_NICKNAME_ALREADY_EXIST.message}. nickname:$nickname",
        cause = null,
    )
}