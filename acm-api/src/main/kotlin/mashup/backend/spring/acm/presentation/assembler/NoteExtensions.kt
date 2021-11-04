package mashup.backend.spring.acm.presentation.assembler

import mashup.backend.spring.acm.domain.note.NoteDetailVo
import mashup.backend.spring.acm.domain.note.NoteGroupDetailVo
import mashup.backend.spring.acm.domain.note.NoteGroupSimpleVo
import mashup.backend.spring.acm.domain.note.NoteSimpleVo
import mashup.backend.spring.acm.presentation.api.note.NoteDetailResponse
import mashup.backend.spring.acm.presentation.api.note.NoteGroupDetailResponse
import mashup.backend.spring.acm.presentation.api.note.NoteGroupSimpleResponse
import mashup.backend.spring.acm.presentation.api.note.NoteSimpleResponse

fun NoteGroupSimpleVo.toDto() = NoteGroupSimpleResponse(
    id = this.id,
    name = this.name,
    customName = this.customName,
)

fun NoteGroupDetailVo.toDto() = NoteGroupDetailResponse(
    id = this.id,
    name = this.name,
    customName = this.customName,
    description = this.description,
    notes = this.notes.map { it.toDto() }
)

fun NoteSimpleVo.toDto() = NoteSimpleResponse(
    id = this.id,
    name = this.name,
    description = this.description,
    thumbnailImageUrl = this.thumbnailImageUrl
)

fun NoteDetailVo.toDto() = NoteDetailResponse(
    id = this.id,
    name = this.name,
    description = this.description,
    thumbnailImageUrl = this.thumbnailImageUrl,
    noteGroup = this.noteGroup?.toDto(),
    perfumes = emptyList(),
)