package mashup.backend.spring.acm.presentation.assembler

import mashup.backend.spring.acm.domain.note.NoteGroupSimpleVo
import mashup.backend.spring.acm.presentation.api.note.NoteGroupSimpleResponse

fun NoteGroupSimpleVo.toDto() = NoteGroupSimpleResponse(
    id = this.id,
    name = this.name,
)