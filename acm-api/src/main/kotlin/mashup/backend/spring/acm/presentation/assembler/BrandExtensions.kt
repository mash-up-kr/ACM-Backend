package mashup.backend.spring.acm.presentation.assembler

import mashup.backend.spring.acm.domain.brand.BrandSimpleVo
import mashup.backend.spring.acm.presentation.api.brand.BrandSimpleResponse

fun BrandSimpleVo.toDto() = BrandSimpleResponse(
    id = this.id,
    name = this.name,
    thumbnailImageUrl = this.logoImageUrl,
)
