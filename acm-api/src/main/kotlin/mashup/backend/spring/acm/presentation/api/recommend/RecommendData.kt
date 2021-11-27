package mashup.backend.spring.acm.presentation.api.recommend

data class MainRecommendData(
    val mainRecommend: MainRecommend
)

data class MainRecommend(
    val title: String,
    val popularBrands: List<PopularBrand>,
    val recommendPerfumes: List<SimpleRecommendPerfumes>,
    val recommendNotes : List<RecommendNote>
)

data class SimpleRecommendPerfumes(
    var no: Int = -1,
    val title: String,
    val perfumes: List<SimpleRecommendPerfume>
)

data class RecommendNote(
    val id: Long,
    val name: String,
    val recommendPerfumes: List<SimpleRecommendPerfume>
)

data class PopularBrand(
    val id: Long,
    val name: String,
    val image: String,
    val recommendPerfumes: List<SimpleRecommendPerfume>
)

data class SimpleRecommendPerfume(
    val id: Long,
    val image: String,
    val brand: String,
    val name: String
)
