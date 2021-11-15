package mashup.backend.spring.acm.domain.recommend.note

import mashup.backend.spring.acm.domain.member.MemberDetailVo
import mashup.backend.spring.acm.domain.note.Note
import mashup.backend.spring.acm.domain.note.NoteSimpleVo
import mashup.backend.spring.acm.domain.recommend.RecommenderBuilder
import org.springframework.stereotype.Service

@Service
class NoteRecommenderService(
    private val recommendNotesByNoteGroupIdsService: RecommendNotesByNoteGroupIdsService,
    private val recommendNotesByDefaultService: RecommendNotesByDefaultService
) {
    fun recommendNotesByNoteGroupIds(memberDetailVo: MemberDetailVo, size: Int) = RecommenderBuilder<Note>()
        .recommendService(listOf(
            recommendNotesByNoteGroupIdsService,
            recommendNotesByDefaultService
        ))
        .size(size)
        .build()
        .recommend(memberDetailVo)
        .map { NoteSimpleVo(it) }
}