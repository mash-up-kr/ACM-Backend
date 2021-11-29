package mashup.backend.spring.acm.application.search

import mashup.backend.spring.acm.domain.brand.BrandSimpleVo
import mashup.backend.spring.acm.domain.perfume.PerfumeSimpleVo

data class SearchResponseVo(
    val brands: List<BrandSimpleVo>,
    val perfumes: List<PerfumeSimpleVo>,
)