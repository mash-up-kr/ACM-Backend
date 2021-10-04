package mashup.backend.spring.acm.domain.perfume

data class PerfumeCreateVo(
    val name: String,
    val originalName: String,
    val brand: String,
    val originalBrand: String,
    val gender: Gender,
    val url: String,
    val thumbnailImageUrl: String
)
