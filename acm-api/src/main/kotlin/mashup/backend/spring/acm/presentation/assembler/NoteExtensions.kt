package mashup.backend.spring.acm.presentation.assembler

import mashup.backend.spring.acm.domain.note.NoteGroupDetailVo
import mashup.backend.spring.acm.domain.note.NoteGroupSimpleVo
import mashup.backend.spring.acm.domain.note.NoteSimpleVo
import mashup.backend.spring.acm.presentation.api.note.NoteGroupDetailResponse
import mashup.backend.spring.acm.presentation.api.note.NoteGroupSimpleResponse
import mashup.backend.spring.acm.presentation.api.note.NoteSimpleResponse

fun NoteGroupSimpleVo.toDto() = NoteGroupSimpleResponse(
    id = this.id,
    name = this.name,
)

fun NoteGroupDetailVo.toDto() = NoteGroupDetailResponse(
    id = this.id,
    name = this.name,
    description = this.description,
    notes = this.notes.map { it.toDto() }
)

fun NoteSimpleVo.toDto() = NoteSimpleResponse(
    id = this.id,
    name = this.name,
    description = this.description,
    thumbnailImageUrl = this.thumbnailImageUrl
)