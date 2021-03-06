package mashup.backend.spring.acm.domain.brand

data class BrandCreateVo(
    val name: String,
    val originalName: String,
    val url: String,
    val description: String,
    val logoImageUrl: String? = null,
    val countryName: String? = null,
    val websiteUrl: String? = null,
    val parentCompanyUrl: String? = null,
)
