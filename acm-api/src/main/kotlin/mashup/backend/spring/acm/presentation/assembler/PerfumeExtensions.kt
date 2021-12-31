package mashup.backend.spring.acm.presentation.assembler

import mashup.backend.spring.acm.domain.perfume.Perfume
import mashup.backend.spring.acm.domain.perfume.PerfumeAccord
import mashup.backend.spring.acm.domain.perfume.PerfumeNoteType
import mashup.backend.spring.acm.domain.perfume.PerfumeSimpleVo
import mashup.backend.spring.acm.presentation.api.perfume.PerfumeDetail
import mashup.backend.spring.acm.presentation.api.perfume.PerfumeSimpleResponse
import mashup.backend.spring.acm.presentation.api.perfume.SimplePerfumeAccord
import mashup.backend.spring.acm.presentation.api.perfume.SimplePerfumeNotes

fun Perfume.toPerfumeDetail(similarPerfumes: List<PerfumeSimpleVo>): PerfumeDetail {
    val typeToNoteNameListMap = this.notes.groupBy(
        { it.noteType },
        { it.note.name }
    )

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
            top = typeToNoteNameListMap[PerfumeNoteType.TOP] ?: listOf(),
            middle = typeToNoteNameListMap[PerfumeNoteType.MIDDLE] ?: listOf(),
            base = typeToNoteNameListMap[PerfumeNoteType.BASE] ?: listOf(),
            unknown = typeToNoteNameListMap[PerfumeNoteType.UNKNOWN] ?: listOf(),
        ),
        similarPerfumes = similarPerfumes.map { it.toDto() },
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
    brandName = this.brandName,
)
