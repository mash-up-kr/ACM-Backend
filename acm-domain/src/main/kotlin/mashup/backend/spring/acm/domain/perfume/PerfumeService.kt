package mashup.backend.spring.acm.domain.perfume

import mashup.backend.spring.acm.domain.accord.AccordService
import mashup.backend.spring.acm.domain.exception.DuplicatedPerfumeException
import mashup.backend.spring.acm.domain.exception.PerfumeNotFoundException
import mashup.backend.spring.acm.domain.note.NoteService
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface PerfumeService {
    fun create(perfumeCreateVo: PerfumeCreateVo): Perfume
    fun getPerfume(id: Long): Perfume
    fun getPerfumesByBrandIdWithRandom(brandId: Long, size: Int): List<PerfumeSimpleVo>
    fun getPerfumesByGenderWithRandom(gender: Gender, size: Int): List<PerfumeSimpleVo>
    fun getPerfumesByNoteId(noteId: Long, size: Int): List<PerfumeSimpleVo>
    fun getPerfumesByNoteIdAndGender(noteId: Long, gender: Gender, size: Int): List<PerfumeSimpleVo>
    fun getSimilarPerfume(id: Long): List<Perfume>
    fun searchByName(name: String): List<PerfumeSimpleVo>
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
        // accord
        perfumeCreateVo.perfumeAccordCreateVoList.forEach {
            createPerfumeAccordIfNotExists(
                perfume = perfume,
                perfumeAccordCreateVo = it
            )
        }
        // note
        perfumeCreateVo.perfumeNoteCreateVoList.forEach { vo ->
            val note = noteService.getNote(vo.noteUrl)
            val perfumeNote = perfume.notes.firstOrNull { it.note === note } ?: PerfumeNote(
                perfume = perfume,
                note = note,
                noteType = vo.noteType
            )
            perfumeNote.noteType = vo.noteType
            perfumeNoteRepository.save(perfumeNote)
        }
        // brand
        perfumeCreateVo.brand?.run {
            perfume.brand = this
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

    @Transactional(readOnly = true)
    override fun getPerfume(id: Long) = perfumeRepository.findPerfumeById(id)
        ?: throw PerfumeNotFoundException("Perfume not found. id: $id")


    private fun getPerfume(url: String) = perfumeRepository.findByUrl(url)
        ?: throw PerfumeNotFoundException("Perfume not found. url: $url")

    override fun getPerfumesByBrandIdWithRandom(brandId: Long, size: Int) =
        perfumeRepository.findByBrand_IdOrderByRandom(brandId, PageRequest.ofSize(size)).map { PerfumeSimpleVo(it) }

    override fun getPerfumesByGenderWithRandom(gender: Gender, size: Int) =
        perfumeRepository.findByGenderOrderByRandom(gender, PageRequest.ofSize(size)).map { PerfumeSimpleVo(it) }


    override fun getPerfumesByNoteId(noteId: Long, size: Int): List<PerfumeSimpleVo> {
        return perfumeNoteRepository.findByNote_Id(noteId, PageRequest.of(0, size)).content
            .map { PerfumeSimpleVo(it.perfume) }
    }

    override fun getPerfumesByNoteIdAndGender(noteId: Long, gender: Gender, size: Int): List<PerfumeSimpleVo> {
        return perfumeNoteRepository.findByNote_IdAndPerfume_Gender(noteId, gender, PageRequest.of(0, size)).content
            .map { PerfumeSimpleVo(it.perfume) }
    }

    @Transactional(readOnly = true)
    override fun getSimilarPerfume(id: Long): List<Perfume> {
        // TODO: 구현해야 함.
        return emptyList()
    }

    override fun searchByName(name: String): List<PerfumeSimpleVo> = perfumeRepository.findTop30ByNameContaining(name)
        .map { PerfumeSimpleVo(it) }
}
