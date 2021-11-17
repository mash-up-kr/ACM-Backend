package mashup.backend.spring.acm.domain.recommend.note

import mashup.backend.spring.acm.domain.note.Note
import mashup.backend.spring.acm.domain.note.NoteGroupService
import mashup.backend.spring.acm.domain.recommend.RecommendRequestVo
import org.springframework.stereotype.Service

@Service
class RecommendNotesByNoteGroupIdsService(
    private val noteGroupService: NoteGroupService
): RecommendNotesService {
    override fun supports(recommendRequestVo: RecommendRequestVo): Boolean {
        val member = recommendRequestVo.memberDetailVo ?: return false

        return member.hasNoteGroupIds()
    }

    override fun getItems(recommendRequestVo: RecommendRequestVo): List<Note> {
        val member = recommendRequestVo.memberDetailVo!!
        return member.noteGroupIds.mapNotNull { noteGroupService.getById(it)?.notes }
            .flatten()
            .distinctBy { it.id }
    }
}