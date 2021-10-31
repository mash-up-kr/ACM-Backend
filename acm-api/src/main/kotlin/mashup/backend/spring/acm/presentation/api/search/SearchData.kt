package mashup.backend.spring.acm.presentation.api.search

import mashup.backend.spring.acm.presentation.api.brand.BrandSimpleResponse
import mashup.backend.spring.acm.presentation.api.perfume.PerfumeSimpleResponse
import javax.validation.constraints.NotBlank

data class SearchData(
    val brands: List<BrandSimpleResponse>,
    val perfumes: List<PerfumeSimpleResponse>,
)

data class SearchRequest(
    @field:NotBlank
    val name: String?,
)