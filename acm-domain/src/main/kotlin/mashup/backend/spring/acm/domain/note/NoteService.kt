package mashup.backend.spring.acm.domain.note

import mashup.backend.spring.acm.domain.exception.DuplicatedNoteException
import mashup.backend.spring.acm.domain.exception.NoteNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface NoteService {
    fun getAllNotes(): List<Note>
    fun getNotes(pageable: Pageable): Page<Note>
    fun getNote(url: String): Note
    fun getFirstNoteToScrap(): Note?
    fun getNoteDetail(noteId: Long): NoteDetailVo
    fun create(noteCreateVo: NoteCreateVo): Note
    fun update(noteId: Long, noteUpdateVo: NoteUpdateVo): Note
}

@Service
@Transactional(readOnly = true)
class NoteServiceImpl(
    private val noteRepository: NoteRepository,
    private val noteGroupService: NoteGroupService,
) : NoteService {
    override fun getAllNotes(): List<Note> {
        return noteRepository.findAll()
    }

    override fun getNotes(pageable: Pageable): Page<Note> = noteRepository.findAll(pageable)

    override fun getNote(url: String): Note = noteRepository.findByUrl(url) ?: throw NoteNotFoundException()

    override fun getFirstNoteToScrap(): Note? = noteRepository.findFirstByNoteGroupIsNull()

    override fun getNoteDetail(noteId: Long): NoteDetailVo = noteRepository.findByIdOrNull(noteId)
        ?.let { NoteDetailVo(it) }
        ?: throw NoteNotFoundException(noteId = noteId)

    @Transactional
    override fun create(noteCreateVo: NoteCreateVo): Note {
        if (noteRepository.existsByUrl(noteCreateVo.url)) {
            throw DuplicatedNoteException("Note already exists. url: ${noteCreateVo.url}")
        }
        return noteRepository.save(Note.from(noteCreateVo))
    }

    @Transactional
    override fun update(noteId: Long, noteUpdateVo: NoteUpdateVo): Note {
        val note = noteRepository.findByIdOrNull(noteId) ?: throw NoteNotFoundException("????????? ????????????. noteId: $noteId")
        if (noteUpdateVo.noteGroupName.isBlank()) {
            throw RuntimeException("?????? ????????? ????????????. noteGroupName: ${noteUpdateVo.noteGroupName}")
        }
        val noteGroup = noteGroupService.getNoteGroupByName(noteUpdateVo.noteGroupName)
            ?: throw RuntimeException("?????? ????????? ????????????. noteGroupName: ${noteUpdateVo.noteGroupName}")
        note.description = noteUpdateVo.description
        note.noteGroup = noteGroup
        return note
    }
}
