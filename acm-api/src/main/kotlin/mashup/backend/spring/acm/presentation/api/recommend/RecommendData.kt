package mashup.backend.spring.acm.presentation.api.recommend

import mashup.backend.spring.acm.domain.perfume.PerfumeSimpleVo

data class MainPopularResponse(
    val mainPopular: MainPopular
)

data class MainPopular(
    val myRecommendPerfumes: List<SimpleRecommendPerfume>,
    val popularBrands: List<PopularBrand>,
    val popularGenderOrRecommendMonthPerfumes: List<SimpleRecommendPerfume>,
    val popularPerfumes: List<SimpleRecommendPerfume>,
    val recommendGiftPerfumesOrRecommendNoteGroupPerfumes: List<SimpleRecommendPerfume>,
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

val SAMPLE_RECOMMEND_PERFUMES: List<SimpleRecommendPerfume> = listOf(
    SimpleRecommendPerfume(
        id = -1,
        image ="https://fimgs.net/mdimg/perfume/375x500.33519.jpg",
        brand = "Maison Francis Kurkdjian",
        name = "Baccarat Rouge 540"
    ),
    SimpleRecommendPerfume(
        id = -1,
        image ="https://fimgs.net/mdimg/perfume/375x500.707.jpg",
        brand = "Mugler",
        name = "Alien"
    ),
    SimpleRecommendPerfume(
        id = -1,
        image ="https://fimgs.net/mdimg/perfume/375x500.1825.jpg",
        brand = "Tom Ford",
        name = "Tobacco Vanille"
    ),
    SimpleRecommendPerfume(
        id = -1,
        image ="https://fimgs.net/mdimg/perfume/375x500.25324.jpg",
        brand = "Saint Laurent",
        name = "Black Opium Yves"
    ),
    SimpleRecommendPerfume(
        id = -1,
        image ="https://fimgs.net/mdimg/perfume/375x500.9828.jpg",
        brand = "Creed",
        name = "Aventus"
    ),
    SimpleRecommendPerfume(
        id = -1,
        image ="https://fimgs.net/mdimg/perfume/375x500.1018.jpg",
        brand = "Tom Ford",
        name = "Black Orchid"
    ),
    SimpleRecommendPerfume(
        id = -1,
        image ="https://fimgs.net/mdimg/perfume/375x500.611.jpg",
        brand = "Chanel",
        name = "Coco Mademoiselle"
    ),
    SimpleRecommendPerfume(
        id = -1,
        image ="https://fimgs.net/mdimg/perfume/375x500.34696.jpg",
        brand = "Armaf",
        name = "Club de Nuit Intense Man"
    ),
    SimpleRecommendPerfume(
        id = -1,
        image ="https://fimgs.net/mdimg/perfume/375x500.29727.jpg",
        brand = "Giorgio Armani",
        name = "Acqua di Giò Profumo"
    ),
    SimpleRecommendPerfume(
        id = -1,
        image ="https://fimgs.net/mdimg/perfume/375x500.31861.jpg",
        brand = "Dior",
        name = "Sauvage"
    ),
)