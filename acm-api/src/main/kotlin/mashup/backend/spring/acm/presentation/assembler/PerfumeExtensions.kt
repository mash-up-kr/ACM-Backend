package mashup.backend.spring.acm.presentation.assembler

import mashup.backend.spring.acm.domain.perfume.PerfumeSimpleVo
import mashup.backend.spring.acm.presentation.api.perfume.PerfumeSimpleResponse
import mashup.backend.spring.acm.presentation.api.recommend.SimpleRecommendPerfume

fun PerfumeSimpleVo.toDto() = PerfumeSimpleResponse(
    id = this.id,
    name = this.name,
    thumbnailImageUrl = this.thumbnailImageUrl,
)

fun PerfumeSimpleVo.toSimpleRecommendPerfume() = SimpleRecommendPerfume(
id = this.id,
image = this.thumbnailImageUrl,
brand = this.brand,
name = this.name
)