package mashup.backend.spring.acm.presentation.api.brand

data class BrandSearchRequest(
    val name: String,
)

data class BrandSearchResponse(
    val brands: List<BrandSimpleResponse>,
)

data class BrandSimpleResponse(
    val id: Long,
    val name: String,
)
