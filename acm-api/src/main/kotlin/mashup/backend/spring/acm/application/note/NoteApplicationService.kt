package mashup.backend.spring.acm.application.note

import mashup.backend.spring.acm.application.ApplicationService
import mashup.backend.spring.acm.domain.note.NoteService
import mashup.backend.spring.acm.presentation.api.note.NoteDetailResponse
import mashup.backend.spring.acm.presentation.assembler.toDto

@ApplicationService
class NoteApplicationService(
    private val noteService: NoteService,
) {
    fun getNote(noteId: Long): NoteDetailResponse = noteService.getNoteDetail(noteId = noteId).toDto()
}