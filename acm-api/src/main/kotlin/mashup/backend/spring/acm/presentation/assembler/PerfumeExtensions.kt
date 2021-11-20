package mashup.backend.spring.acm.presentation.assembler

import mashup.backend.spring.acm.domain.perfume.Perfume
import mashup.backend.spring.acm.domain.perfume.PerfumeAccord
import mashup.backend.spring.acm.domain.perfume.PerfumeNoteType
import mashup.backend.spring.acm.domain.perfume.PerfumeSimpleVo
import mashup.backend.spring.acm.presentation.api.perfume.*
import mashup.backend.spring.acm.presentation.api.recommend.SimpleRecommendPerfume

fun Perfume.toPerfumeDetail(similarPerfumes: List<SimpleRecommendPerfume>): PerfumeDetail {
    val topNotes = mutableListOf<String>()
    val middleNotes = mutableListOf<String>()
    val baseNotes = mutableListOf<String>()
    val unknownNotes = mutableListOf<String>()
    for (perfumeNote in this.notes) {
        when (perfumeNote.noteType) {
            PerfumeNoteType.TOP -> topNotes.add(perfumeNote.note.name)
            PerfumeNoteType.MIDDLE -> middleNotes.add(perfumeNote.note.name)
            PerfumeNoteType.BASE -> baseNotes.add(perfumeNote.note.name)
            PerfumeNoteType.UNKNOWN -> unknownNotes.add(perfumeNote.note.name)
        }
    }

    return PerfumeDetail(
        id = this.id,
        name = this.name,
        brandName = this.brand?.name ?: "",
        gender = this.gender,
        description = this.description,
        imageUrl = this.imageUrl,
        thumbnailImageUrl = this.thumbnailImageUrl,
        accords = this.accords.map { it.toSimplePerfumeAccord() },
        notes = SimplePerfumeNotes(
            top = topNotes,
            middle = middleNotes,
            base = baseNotes,
            unknown = unknownNotes
        ),
        similarPerfumes = similarPerfumes
    )
}

fun PerfumeAccord.toSimplePerfumeAccord() = SimplePerfumeAccord(
    name = this.accord.name,
    score = this.width,
    opacity = this.opacity,
    backgroundColor = this.accord.backgroundColor,
    textColor = this.accord.textColor
)

fun PerfumeSimpleVo.toDto() = PerfumeSimpleResponse(
    id = this.id,
    name = this.name,
    thumbnailImageUrl = this.thumbnailImageUrl,
)

fun PerfumeSimpleVo.toSimpleRecommendPerfume() = SimpleRecommendPerfume(
id = this.id,
image = this.thumbnailImageUrl,
brand = this.brandName,
name = this.name
)