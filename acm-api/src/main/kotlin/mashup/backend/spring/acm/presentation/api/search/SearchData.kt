package mashup.backend.spring.acm.presentation.api.search

import mashup.backend.spring.acm.presentation.api.brand.BrandSimpleResponse
import mashup.backend.spring.acm.presentation.api.perfume.PerfumeSimpleResponse

data class SearchData(
    val brands: List<BrandSimpleResponse>,
    val perfumes: List<PerfumeSimpleResponse>,
)

data class SearchRequest(
    val name: String,
)