package mashup.backend.spring.acm.domain.recommend.note

import mashup.backend.spring.acm.domain.note.Note
import mashup.backend.spring.acm.domain.note.NoteGroupService
import mashup.backend.spring.acm.domain.recommend.RecommendRequestVo
import org.slf4j.Logger
import org.slf4j.LoggerFactory
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

        var notes = member.noteGroupIds.mapNotNull { noteGroupService.getById(it)?.notes }
            .flatten()
            .distinctBy { it.id }
            .shuffled()

        if (notes.size > recommendRequestVo.size) {
            notes = notes.subList(0, recommendRequestVo.size)
        }

        return notes
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(this::class.java)
    }
}