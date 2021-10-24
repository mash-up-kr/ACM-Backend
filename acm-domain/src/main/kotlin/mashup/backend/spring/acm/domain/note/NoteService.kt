package mashup.backend.spring.acm.domain.note

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface NoteService {
    fun getNotes(pageable: Pageable): Page<Note>
    fun getNote(url: String): Note
    fun create(noteCreateVo: NoteCreateVo): Note
}

@Service
@Transactional(readOnly = true)
class NoteServiceImpl(
    private val noteRepository: NoteRepository,
    private val noteGroupService: NoteGroupService,
) : NoteService {
    override fun getNotes(pageable: Pageable): Page<Note> = noteRepository.findAll(pageable)

    override fun getNote(url: String): Note = noteRepository.findByUrl(url) ?: throw NoteNotFoundException()

    @Transactional
    override fun create(noteCreateVo: NoteCreateVo): Note {
        if (noteRepository.existsByUrl(noteCreateVo.url)) {
            throw DuplicatedNoteException("Note already exists. url: ${noteCreateVo.url}")
        }
        val noteGroup = noteGroupService.getNoteGroupByName(noteCreateVo.noteGroupName)
            ?: throw RuntimeException("노트 그룹이 없습니다. noteGroupName: ${noteCreateVo.noteGroupName}")
        return noteRepository.save(Note.of(noteCreateVo, noteGroup))
    }
}
