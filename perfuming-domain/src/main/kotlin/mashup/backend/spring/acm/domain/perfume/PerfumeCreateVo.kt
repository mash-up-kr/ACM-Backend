package mashup.backend.spring.acm.domain.perfume

data class PerfumeCreateVo(
    val name: String,
    val description: String,
    val url: String,
    val thumbnailImageUrl: String
)
