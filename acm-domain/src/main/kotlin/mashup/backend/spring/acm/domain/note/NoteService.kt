package mashup.backend.spring.acm.domain.note

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface NoteService {
    fun getNotes(pageable: Pageable): Page<Note>
    fun create(noteCreateVo: NoteCreateVo): Note
}

@Service
@Transactional(readOnly = true)
class NoteServiceImpl(
    private val noteRepository: NoteRepository
) : NoteService {
    override fun getNotes(pageable: Pageable): Page<Note> = noteRepository.findAll(pageable)

    @Transactional
    override fun create(noteCreateVo: NoteCreateVo): Note {
        if (noteRepository.existsByUrl(noteCreateVo.url)) {
            throw DuplicatedNoteException(message = "Note already exists. url: ${noteCreateVo.url}")
        }
        return noteRepository.save(Note.from(noteCreateVo))
    }
}
