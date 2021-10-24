package mashup.backend.spring.acm.domain.note

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface NoteService {
    fun getNotes(pageable: Pageable): Page<Note>
    fun getNote(url: String): Note
    fun getFirstNoteToScrap(): Note?
    fun create(noteCreateVo: NoteCreateVo): Note
    fun update(noteId: Long, noteUpdateVo: NoteUpdateVo): Note
}

@Service
@Transactional(readOnly = true)
class NoteServiceImpl(
    private val noteRepository: NoteRepository,
    private val noteGroupService: NoteGroupService,
) : NoteService {
    override fun getNotes(pageable: Pageable): Page<Note> = noteRepository.findAll(pageable)

    override fun getNote(url: String): Note = noteRepository.findByUrl(url) ?: throw NoteNotFoundException()

    override fun getFirstNoteToScrap(): Note? = noteRepository.findFirstByNoteGroupIsNull()

    @Transactional
    override fun create(noteCreateVo: NoteCreateVo): Note {
        if (noteRepository.existsByUrl(noteCreateVo.url)) {
            throw DuplicatedNoteException("Note already exists. url: ${noteCreateVo.url}")
        }
        return noteRepository.save(Note.from(noteCreateVo))
    }

    @Transactional
    override fun update(noteId: Long, noteUpdateVo: NoteUpdateVo): Note {
        val note = noteRepository.findByIdOrNull(noteId) ?: throw NoteNotFoundException("노트가 없습니다. noteId: $noteId")
        if (noteUpdateVo.noteGroupName.isBlank()) {
            throw RuntimeException("노트 그룹이 없습니다. noteGroupName: ${noteUpdateVo.noteGroupName}")
        }
        val noteGroup = noteGroupService.getNoteGroupByName(noteUpdateVo.noteGroupName)
            ?: throw RuntimeException("노트 그룹이 없습니다. noteGroupName: ${noteUpdateVo.noteGroupName}")
        note.description = noteUpdateVo.description
        note.noteGroup = noteGroup
        return note
    }
}
