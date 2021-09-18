package mashup.backend.spring.acm.domain.perfume

import mashup.backend.spring.acm.domain.note.NoteService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface PerfumeService {
    fun create(perfumeCreateVo: PerfumeCreateVo): Perfume
    fun add(perfumeUrl: String, noteUrl: String, noteType: PerfumeNoteType)
}

@Service
@Transactional(readOnly = true)
class PerfumeServiceImpl(
    private val perfumeRepository: PerfumeRepository,
    private val noteService: NoteService
) : PerfumeService {
    @Transactional
    override fun create(perfumeCreateVo: PerfumeCreateVo): Perfume {
        if (perfumeRepository.existsByUrl(perfumeCreateVo.url)) {
            throw DuplicatedPerfumeException("Perfume already exists. url: ${perfumeCreateVo.url}")
        }
        return perfumeRepository.save(Perfume.from(perfumeCreateVo))
    }

    @Transactional
    override fun add(perfumeUrl: String, noteUrl: String, noteType: PerfumeNoteType) {
        val perfume = getPerfume(perfumeUrl)
        val note = noteService.getNote(noteUrl)
        val perfumeNote = perfume.notes.firstOrNull { it.note === note } ?: PerfumeNote(
            perfume = perfume,
            note = note,
            noteType = noteType
        )
        perfumeNote.noteType = noteType
        perfumeRepository.save(perfume)
    }

    private fun getPerfume(url: String) = perfumeRepository.findByUrl(url)
        ?: throw PerfumeNotFoundException("Perfume not found. url: $url")
}
