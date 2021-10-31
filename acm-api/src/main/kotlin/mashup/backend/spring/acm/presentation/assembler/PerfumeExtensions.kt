package mashup.backend.spring.acm.presentation.assembler

import mashup.backend.spring.acm.domain.perfume.PerfumeSimpleVo
import mashup.backend.spring.acm.presentation.api.perfume.PerfumeSimpleResponse

fun PerfumeSimpleVo.toDto() = PerfumeSimpleResponse(
    id = this.id,
    name = this.name,
    thumbnailImageUrl = this.thumbnailImageUrl,
)
