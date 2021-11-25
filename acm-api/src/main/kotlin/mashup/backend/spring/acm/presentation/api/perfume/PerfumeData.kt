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
