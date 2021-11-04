package mashup.backend.spring.acm.domain.perfume

import mashup.backend.spring.acm.domain.brand.Brand

data class PerfumeCreateVo(
    val name: String,
    val originalName: String,
    val gender: Gender,
    val url: String,
    val thumbnailImageUrl: String,
    val imageUrl: String = "",
    val description: String = "",
    val perfumeAccordCreateVoList: List<PerfumeAccordCreateVo> = emptyList(),
    val perfumeNoteCreateVoList: List<PerfumeNoteCreateVo> = emptyList(),
    val brand: Brand? = null,
)
