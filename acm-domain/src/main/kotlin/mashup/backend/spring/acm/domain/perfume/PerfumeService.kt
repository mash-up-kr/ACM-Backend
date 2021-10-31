package mashup.backend.spring.acm.domain.perfume

import mashup.backend.spring.acm.domain.accord.AccordService
import mashup.backend.spring.acm.domain.brand.Brand
import mashup.backend.spring.acm.domain.exception.DuplicatedPerfumeException
import mashup.backend.spring.acm.domain.exception.PerfumeNotFoundException
import mashup.backend.spring.acm.domain.note.NoteService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface PerfumeService {
    fun create(perfumeCreateVo: PerfumeCreateVo): Perfume
    fun add(perfumeUrl: String, noteUrl: String, noteType: PerfumeNoteType)
    fun setBrand(perfumeUrl: String, brand: Brand)
    fun getPerfume(id: Long): Perfume
    fun getSimilarPerfume(id: Long): List<Perfume>
}

@Service
@Transactional(readOnly = true)
class PerfumeServiceImpl(
    private val perfumeRepository: PerfumeRepository,
    private val perfumeAccordRepository: PerfumeAccordRepository,
    private val perfumeNoteRepository: PerfumeNoteRepository,
    private val noteService: NoteService,
    private val accordService: AccordService,
) : PerfumeService {
    @Transactional
    override fun create(perfumeCreateVo: PerfumeCreateVo): Perfume {
        if (perfumeRepository.existsByUrl(perfumeCreateVo.url)) {
            throw DuplicatedPerfumeException("Perfume already exists. url: ${perfumeCreateVo.url}")
        }
        val perfume = perfumeRepository.save(Perfume.from(perfumeCreateVo))
        perfumeCreateVo.perfumeAccordCreateVoList.forEach {
            createPerfumeAccordIfNotExists(
                perfume = perfume,
                perfumeAccordCreateVo = it
            )
        }
        return perfume
    }

    private fun createPerfumeAccordIfNotExists(
        perfume: Perfume,
        perfumeAccordCreateVo: PerfumeAccordCreateVo,
    ): PerfumeAccord {
        val accord = accordService.getById(accordId = perfumeAccordCreateVo.accordId)
        val perfumeAccord = perfumeAccordRepository.findByPerfumeAndAccord(perfume = perfume, accord = accord)
        if (perfumeAccord != null) {
            return perfumeAccord
        }
        return perfumeAccordRepository.save(PerfumeAccord(
            width = perfumeAccordCreateVo.width,
            opacity = perfumeAccordCreateVo.opacity,
            perfume = perfume,
            accord = accord,
        ))
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
        perfumeNoteRepository.save(perfumeNote)
    }

    @Transactional
    override fun setBrand(perfumeUrl: String, brand: Brand) {
        val perfume = getPerfume(url = perfumeUrl)
        if (perfume.brand == brand) {
            return
        }
        perfume.brand = brand
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
}
