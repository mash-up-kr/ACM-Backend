package mashup.backend.spring.acm.presentation.api.perfume

import mashup.backend.spring.acm.domain.perfume.Gender
import mashup.backend.spring.acm.domain.perfume.Perfume
import kotlin.streams.toList

data class PerfumeDetailResponse(
    val perfumeDetail: PerfumeDetail
)

data class PerfumeDetail(
    val id: Long,
    val name: String,
    val brandName: String,
    val gender: Gender,
    val description: String,
    val imageUrl: String,
    val thumbnailImageUrl: String,
    val accords: List<SimplePerfumeAccord>,
    val notes: SimplePerfumeNotes,
    val similarPerfumes: List<SimpleSimilarPerfume> = emptyList()
)

data class SimplePerfumeNotes(
    val top: List<String>,
    val middle: List<String>,
    val base: List<String>,
    val unknown: List<String>
)

data class SimplePerfumeAccord(
    val name: String,
    val score: Double?,
    val opacity: Double?,
    val backgroundColor: String,
    val textColor: String,
)

data class SimpleSimilarPerfume(
    val id: Long,
    val thumbnailImageUrl: String,
    val name: String
) {
    companion object {
        fun of(perfume: List<Perfume>) : List<SimpleSimilarPerfume> {
            return perfume.stream().map(Companion::of).toList()
        }

        private fun of(perfume: Perfume) = SimpleSimilarPerfume(
            id = perfume.id,
            thumbnailImageUrl = perfume.thumbnailImageUrl,
            name = perfume.name
        )
    }
}

data class PerfumeSearchRequest(
    val name: String,
)

data class PerfumeSearchResponse(
    val perfumes: List<PerfumeSimpleResponse>,
)

data class PerfumeSimpleResponse(
    val id: Long,
    val name: String,
    val thumbnailImageUrl: String,
)

val SAMPLE_PERFUME_LIST: List<PerfumeSimpleResponse> = listOf(
    PerfumeSimpleResponse(
        id = -1,
        name = "Tobacco Oud",
        thumbnailImageUrl = "https://fimgs.net/mdimg/perfume/s.21402.jpg",
    ),
    PerfumeSimpleResponse(
        id = -2,
        name = "One-Man-Show-Emerald-Edition",
        thumbnailImageUrl = "https://fimgs.net/mdimg/perfume/s.46964.jpg"
    ),
    PerfumeSimpleResponse(
        id = -3,
        name = "Armani-Prive-Rouge-Malachite",
        thumbnailImageUrl = "https://fimgs.net/mdimg/perfume/s.34589.jpg",
    ),
    PerfumeSimpleResponse(
        id = -4,
        name = "R.E.M.",
        thumbnailImageUrl = "https://fimgs.net/mdimg/perfume/s.62578.jpg",
    ),
    PerfumeSimpleResponse(
        id = -5,
        name = "L'eau À la Rose",
        thumbnailImageUrl = "https://fimgs.net/mdimg/perfume/s.55118.jpg",
    ),
    PerfumeSimpleResponse(
        id = -6,
        name = "Narciso",
        thumbnailImageUrl = "https://fimgs.net/mdimg/perfume/s.26127.jpg",
    ),
    PerfumeSimpleResponse(
        id = -7,
        name = "1 Million Prive",
        thumbnailImageUrl = "https://fimgs.net/mdimg/perfume/s.37698.jpg",
    ),
    PerfumeSimpleResponse(
        id = -8,
        name = "Tres Chere",
        thumbnailImageUrl = "https://fimgs.net/mdimg/perfume/s.45116.jpg",
    ),
    PerfumeSimpleResponse(
        id = -9,
        name = "Do Son Eau de Parfum",
        thumbnailImageUrl = "https://fimgs.net/mdimg/perfume/s.49098.jpg",
    ),
    PerfumeSimpleResponse(
        id = -10,
        name = "Club-de-Nuit-Intense",
        thumbnailImageUrl = "https://fimgs.net/mdimg/perfume/s.27656.jpg",
    ),
)
