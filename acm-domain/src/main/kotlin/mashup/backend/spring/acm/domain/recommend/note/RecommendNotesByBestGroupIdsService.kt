package mashup.backend.spring.acm.domain.recommend.note

import mashup.backend.spring.acm.domain.note.Note
import mashup.backend.spring.acm.domain.note.NoteGroupService
import mashup.backend.spring.acm.domain.recommend.RecommendRequestVo
import mashup.backend.spring.acm.infrastructure.CacheType
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class RecommendNotesByDefaultService(
    private val noteGroupService: NoteGroupService
): RecommendNotesService {
    override fun supports(recommendRequestVo: RecommendRequestVo): Boolean {
        return true
    }

    @Cacheable(value = [CacheType.CacheNames.RECOMMEND_DEFAULT_NOTES], key = "#recommendRequestVo.size")
    override fun getItems(recommendRequestVo: RecommendRequestVo): List<Note> {
        return noteGroupService.getByRandom(recommendRequestVo.size)
            .map { it.notes }
            .flatten()
            .distinctBy { it.id }
    }
}