package mashup.backend.spring.acm.domain.perfume

import mashup.backend.spring.acm.domain.exception.DuplicatedPerfumeException
import mashup.backend.spring.acm.domain.exception.PerfumeNotFoundException
import mashup.backend.spring.acm.domain.note.NoteService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface PerfumeService {
    fun create(perfumeCreateVo: PerfumeCreateVo): Perfume
    fun add(perfumeUrl: String, noteUrl: String, noteType: PerfumeNoteType)
    fun getPerfume(id: Long): Perfume
    fun getSimilarPerfume(id: Long): List<Perfume>
    fun searchByName(name: String): List<PerfumeSimpleVo>
}

@Service
@Transactional(readOnly = true)
class PerfumeServiceImpl(
    private val perfumeRepository: PerfumeRepository,
    private val noteService: NoteService,
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

    @Transactional(readOnly = true)
    override fun getPerfume(id: Long) = perfumeRepository.findPerfumeById(id)
        ?: throw PerfumeNotFoundException("Perfume not found. id: $id")


    private fun getPerfume(url: String) = perfumeRepository.findByUrl(url)
        ?: throw PerfumeNotFoundException("Perfume not found. url: $url")

    @Transactional(readOnly = true)
    override fun getSimilarPerfume(id: Long): List<Perfume> {
        // TODO: 구현해야 함.
        return emptyList()
    }

    override fun searchByName(name: String): List<PerfumeSimpleVo> = perfumeRepository.findByNameContaining(name)
        .map { PerfumeSimpleVo(it) }
}
