package mashup.backend.spring.acm.domain.exception

import mashup.backend.spring.acm.domain.ResultCode

/**
 * 조회 실패시 발생하는 예외
 * api 서버에서 http status code 404 로 응답함
 */
abstract class NotFoundException(
    override val resultCode: ResultCode,
    override val message: String?,
    override val cause: Throwable?,
) : BusinessException(
    resultCode = resultCode,
    message = message,
    cause = cause
)
