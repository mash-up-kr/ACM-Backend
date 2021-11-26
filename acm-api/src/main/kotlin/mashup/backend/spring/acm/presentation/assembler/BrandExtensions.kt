package mashup.backend.spring.acm.presentation.assembler

import mashup.backend.spring.acm.domain.brand.BrandDetailVo
import mashup.backend.spring.acm.domain.brand.BrandSimpleVo
import mashup.backend.spring.acm.presentation.api.brand.BrandDetailResponse
import mashup.backend.spring.acm.presentation.api.brand.BrandSimpleResponse
import mashup.backend.spring.acm.presentation.api.recommend.PopularBrand
import mashup.backend.spring.acm.presentation.api.recommend.SimpleRecommendPerfume

fun BrandSimpleVo.toDto() = BrandSimpleResponse(
    id = this.id,
    name = this.name,
    thumbnailImageUrl = this.logoImageUrl,
)

fun BrandSimpleVo.toPopularBrand(perfumes: List<SimpleRecommendPerfume>) = PopularBrand(
    id = this.id,
    name = this.name,
    image = this.logoImageUrl,
    recommendPerfumes = perfumes
)

fun BrandDetailVo.toDto() = BrandDetailResponse(
    id = this.id,
    name = this.name,
    thumbnailImageUrl = this.thumbnailImageUrl,
    perfumes = perfumeSimpleVoList.map { it.toDto() },
)