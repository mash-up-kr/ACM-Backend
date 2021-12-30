package mashup.backend.spring.acm.presentation.api.recommend

data class MainRecommendData(
    val mainRecommend: MainRecommend,
)

data class MainRecommend(
    val hasOnboarded: Boolean,
    val title: String,
    val popularBrands: List<PopularBrand>,
    val recommendPerfumes: List<SimpleRecommendPerfumes>,
    val recommendNoteGroups: List<RecommendNoteGroup>,
)

data class SimpleRecommendPerfumes(
    var no: Int = -1,
    val title: String,
    val perfumes: List<SimpleRecommendPerfume>,
)

data class RecommendNoteGroup(
    val id: Long,
    val name: String,
    val recommendNotes: List<RecommendNote>,
)

data class RecommendNote(
    val id: Long,
    val name: String,
    val recommendPerfumes: List<SimpleRecommendPerfume>,
)

data class PopularBrand(
    val id: Long,
    val name: String,
    val image: String,
    val recommendPerfumes: List<SimpleRecommendPerfume>,
)

data class SimpleRecommendPerfume(
    val id: Long,
    @Deprecated("Use 'thumbnailImageUrl' instead")
    val image: String,
    val thumbnailImageUrl: String,
    @Deprecated("Use 'brandName' instead")
    val brand: String,
    val brandName: String,
    val name: String,
)
