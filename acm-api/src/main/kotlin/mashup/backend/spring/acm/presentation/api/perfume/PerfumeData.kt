package mashup.backend.spring.acm.presentation.api.perfume

import mashup.backend.spring.acm.domain.perfume.*
import kotlin.streams.toList

data class PerfumeDetailResponse(
    val perfumeDetail: PerfumeDetail
)

data class PerfumeDetail(
    val id: Long,
    val name: String,
    val brand: String,
    val gender: Gender,
    val description: String,
    val imageUrl: String,
    val thumbnailImageUrl: String,
    val accords: List<SimplePerfumeAccord>,
    val notes: List<SimplePerfumeNote>,
    val similarPerfumes: List<SimpleSimilarPerfume> = emptyList()
) {
    companion object {
        fun of(perfume: Perfume, similarPerfumes: List<SimpleSimilarPerfume>) = PerfumeDetail(
            id = perfume.id,
            name = perfume.name,
            brand = perfume.brand?.name ?: "",
            gender = perfume.gender,
            description = perfume.description,
            imageUrl = perfume.imageUrl,
            thumbnailImageUrl = perfume.thumbnailImageUrl,
            accords = SimplePerfumeAccord.of(perfume.accords),
            notes = SimplePerfumeNote.of(perfume.notes),
            similarPerfumes = similarPerfumes
        )
    }
}

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

data class SimplePerfumeAccord(
    val name: String,
    val description: String,
    val score: Double
) {
    companion object {
        fun of(perfumeAccords: List<PerfumeAccord>) : List<SimplePerfumeAccord> {
            return perfumeAccords.stream().map(Companion::of).toList()
        }

        private fun of(perfumeAccord: PerfumeAccord) = SimplePerfumeAccord(
            name = perfumeAccord.accord.name,
            description = "",
            score = perfumeAccord.width ?: 0.0
        )
    }
}

data class SimplePerfumeNote(
    var noteType: PerfumeNoteType,
    val name: String,
    val description: String = "",
    val thumbnailImageUrl: String
) {
    companion object {
        fun of(perfumeNotes: List<PerfumeNote>) : List<SimplePerfumeNote> {
            return perfumeNotes.stream().map(Companion::of).toList()
        }
        private fun of(perfumeNote: PerfumeNote) = SimplePerfumeNote(
            noteType = perfumeNote.noteType,
            name = perfumeNote.note.name,
            description = perfumeNote.note.description,
            thumbnailImageUrl = perfumeNote.note.thumbnailImageUrl
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
