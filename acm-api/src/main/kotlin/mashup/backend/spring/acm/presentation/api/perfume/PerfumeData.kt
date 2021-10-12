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
            brand = perfume.brand,
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
            name = perfumeAccord.perfume.name,
            description = perfumeAccord.perfume.description,
            score = perfumeAccord.score
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
