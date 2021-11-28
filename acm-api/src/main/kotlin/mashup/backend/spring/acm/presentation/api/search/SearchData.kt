package mashup.backend.spring.acm.presentation.api.search

import mashup.backend.spring.acm.presentation.api.brand.BrandSimpleResponse
import mashup.backend.spring.acm.presentation.api.perfume.PerfumeSimpleResponse
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

data class SearchResponse(
    val brands: List<BrandSimpleResponse>,
    val perfumes: List<PerfumeSimpleResponse>,
)

data class SearchRequest(
    @field:NotBlank
    val name: String?,
    @field:NotBlank
    val type: String?,
    @field:Min(0)
    val page: Int? = 0,
    @field:Min(0)
    @field:Max(30)
    val size: Int? = 30,
)

enum class SearchType(
    private val description: String,
) {
    ALL("전체"),
    BRAND("브랜드명"),
    PERFUME("향수명"),
}