package mashup.backend.spring.acm.domain.perfume

data class PerfumeSimpleVo(
    val id: Long,
    val name: String,
    val thumbnailImageUrl: String,
    val brand: String
) {
    constructor(perfume: Perfume) : this(
        id = perfume.id,
        name = perfume.name,
        thumbnailImageUrl = perfume.thumbnailImageUrl,
        brand = perfume.brand!!.name
    )
}