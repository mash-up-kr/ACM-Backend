package mashup.backend.spring.acm.domain.brand

import mashup.backend.spring.acm.domain.perfume.PerfumeSimpleVo

data class BrandDetailVo(
    val id: Long,
    val name: String,
    val thumbnailImageUrl: String,
    val perfumeSimpleVoList: List<PerfumeSimpleVo>,
) {
    constructor(brand: Brand, perfumeSimpleVoList: List<PerfumeSimpleVo>): this(
        id = brand.id,
        name = brand.name,
        thumbnailImageUrl = brand.logoImageUrl ?: "",
        perfumeSimpleVoList = perfumeSimpleVoList.toList(),
    )
}