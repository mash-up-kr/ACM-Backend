package mashup.backend.spring.acm.application.note

import mashup.backend.spring.acm.application.ApplicationService
import mashup.backend.spring.acm.domain.note.NoteGroupDetailVo
import mashup.backend.spring.acm.domain.note.NoteGroupService
import mashup.backend.spring.acm.domain.note.NoteGroupSimpleVo
import mashup.backend.spring.acm.domain.note.NoteSimpleVo

@ApplicationService
class NoteGroupApplicationService(
    private val noteGroupService: NoteGroupService,
) {
    fun getAllNoteGroups(): List<NoteGroupSimpleVo> = noteGroupService.findAll().map { NoteGroupSimpleVo(it) }

    fun getNoteGroup(noteGroupId: Long): NoteGroupDetailVo = noteGroupService.getDetailById(noteGroupId)

    fun getNotes(noteGroupId: Long): List<NoteSimpleVo> =
        noteGroupService.getDetailById(noteGroupId = noteGroupId).notes
}