package mashup.backend.spring.acm.presentation.api.recommend

data class MainPopularResponse(
    val mainPopular: MainPopular
)

data class MainPopular(
    val myRecommendPerfumes: List<SimpleRecommendPerfume>,
    val popularBrands: List<String>,
    val recommendPerfumesList: List<List<SimpleRecommendPerfume>>,
    val recommendNoteGroups : List<RecommendNoteGroup>
)

data class RecommendNoteGroup(
    val name: String,
    val recommendNotes: List<RecommendNote>
)

data class RecommendNote(
    val name: String,
    val images: List<String>,
    val recommendPerfumes: List<SimpleRecommendPerfume>
)

data class SimpleRecommendPerfume(
    val id: Long,
    val image: String,
    val brand: String,
    val name: String
)

