package mashup.backend.spring.acm

import mashup.backend.spring.acm.domain.ResultCode

open class BusinessException(
    val resultCode: ResultCode,
    override val message: String? = "",
    override val cause: Throwable? = null,
) : RuntimeException(message, cause)