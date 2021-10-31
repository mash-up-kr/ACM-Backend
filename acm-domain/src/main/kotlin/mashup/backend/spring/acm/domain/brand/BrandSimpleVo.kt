package mashup.backend.spring.acm.domain.brand

data class BrandSimpleVo(
    val id: Long,
    val name: String,
    val logoImageUrl: String,
) {
    constructor(brand: Brand) : this(
        id = brand.id,
        name = brand.name,
        logoImageUrl = brand.logoImageUrl ?: "",
    )
}