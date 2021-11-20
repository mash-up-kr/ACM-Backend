package mashup.backend.spring.acm.domain.recommend.note

import mashup.backend.spring.acm.domain.note.Note
import mashup.backend.spring.acm.domain.note.NoteGroupService
import mashup.backend.spring.acm.domain.recommend.RecommendRequestVo
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RecommendNotesByDefaultService(
    private val noteGroupService: NoteGroupService
): RecommendNotesService {
    override fun supports(recommendRequestVo: RecommendRequestVo): Boolean {
        return true
    }

    @Transactional
    override fun getItems(recommendRequestVo: RecommendRequestVo): List<Note> {
        return noteGroupService.getByRandom(recommendRequestVo.size)
            .map { it.notes }
            .flatten()
            .distinctBy { it.id }
    }
}