package mashup.backend.spring.acm.domain.exception

import mashup.backend.spring.acm.domain.ResultCode

class BrandNotFoundException(
    override val message: String? = null,
    override val cause: Throwable? = null,
) : NotFoundException(
    resultCode = ResultCode.BRAND_NOT_FOUND,
    message = message,
    cause = cause,
) {
    constructor(brandId: Long) : this(
        message = "브랜드를 찾을 수 없습니다. brandId: $brandId",
    )
}