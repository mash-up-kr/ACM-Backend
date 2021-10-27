package mashup.backend.spring.acm.domain.exception

import mashup.backend.spring.acm.domain.ResultCode

/**
 * 요청에 오류가 있을 때 발생하는 예외
 * api 서버에서 http status code 400 으로 응답함
 */
abstract class BadRequestException(
    override val resultCode: ResultCode,
    override val message: String?,
    override val cause: Throwable?,
) : BusinessException(
    resultCode = resultCode,
    message = message,
    cause = cause
)
