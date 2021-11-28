package mashup.backend.spring.acm.domain.recommend.perfume

import mashup.backend.spring.acm.domain.note.NoteGroupService
import mashup.backend.spring.acm.domain.perfume.Perfume
import mashup.backend.spring.acm.domain.perfume.PerfumeService
import mashup.backend.spring.acm.domain.recommend.RecommendRequestVo
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class RecommendPerfumesByPopularNoteGroupService(
    private val noteGroupService: NoteGroupService,
    private val perfumeService: PerfumeService
): RecommendPerfumesService {
    override fun supports(recommendRequestVo: RecommendRequestVo): Boolean {
        return true
    }

    override fun getItems(recommendRequestVo: RecommendRequestVo): List<Perfume> {
        val noteGroup = noteGroupService.getPopularNoteGroup()
        val size = recommendRequestVo.size
        val perfumes = mutableListOf<Perfume>()
        for (note in noteGroup.notes) {
            if (perfumes.size >= size) break

            perfumeService.getPerfumesByNoteId(note.id, size - perfumes.size)
                .forEach { perfumes.add(it) }
        }

        return perfumes
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(RecommendPerfumesByPopularNoteGroupService::class.java)
    }
}