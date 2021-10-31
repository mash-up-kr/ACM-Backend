package mashup.backend.spring.acm.presentation.assembler

import mashup.backend.spring.acm.domain.brand.BrandDetailVo
import mashup.backend.spring.acm.domain.brand.BrandSimpleVo
import mashup.backend.spring.acm.presentation.api.brand.BrandDetailResponse
import mashup.backend.spring.acm.presentation.api.brand.BrandSimpleResponse
import mashup.backend.spring.acm.presentation.api.perfume.SAMPLE_PERFUME_LIST

fun BrandSimpleVo.toDto() = BrandSimpleResponse(
    id = this.id,
    name = this.name,
    thumbnailImageUrl = this.logoImageUrl,
)

fun BrandDetailVo.toDto() = BrandDetailResponse(
    id = this.id,
    name = this.name,
    thumbnailImageUrl = this.thumbnailImageUrl,
    perfumes = SAMPLE_PERFUME_LIST,
)
