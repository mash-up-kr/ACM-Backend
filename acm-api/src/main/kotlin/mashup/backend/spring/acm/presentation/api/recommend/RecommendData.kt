package mashup.backend.spring.acm.presentation.api.recommend

import mashup.backend.spring.acm.presentation.api.brand.BrandSimpleResponse
import mashup.backend.spring.acm.presentation.api.perfume.PerfumeSimpleResponse

data class MainRecommendData(
    val mainRecommend: MainRecommend,
)

data class MainRecommend(
    val hasOnboarded: Boolean,
    val title: String,
    val popularBrands: List<BrandSimpleResponse>,
    val recommendPerfumes: List<SimpleRecommendPerfumes>,
    val recommendNoteGroups: List<RecommendNoteGroup>,
)

data class SimpleRecommendPerfumes(
    var no: Int = -1,
    val title: String,
    val perfumes: List<PerfumeSimpleResponse>,
)

data class RecommendNoteGroup(
    val id: Long,
    val name: String,
    val notes: List<RecommendNote>,
)

data class RecommendNote(
    val id: Long,
    val name: String,
    val perfumes: List<PerfumeSimpleResponse>,
)
