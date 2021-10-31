package mashup.backend.spring.acm.domain.brand

data class BrandDetailVo(
    val id: Long,
    val name: String,
    val thumbnailImageUrl: String,
) {
    constructor(brand: Brand) : this(
        id = brand.id,
        name = brand.name,
        thumbnailImageUrl = brand.logoImageUrl ?: "",
    )
}