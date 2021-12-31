package mashup.backend.spring.acm.presentation.api.brand

import mashup.backend.spring.acm.presentation.api.perfume.PerfumeSimpleResponse

data class BrandSimpleResponse(
    val id: Long,
    val name: String,
    val thumbnailImageUrl: String,
)

data class BrandDetailData(
    val brand: BrandDetailResponse,
)

data class BrandDetailResponse(
    val id: Long,
    val name: String,
    val thumbnailImageUrl: String?,
    val perfumes: List<PerfumeSimpleResponse>,
)
