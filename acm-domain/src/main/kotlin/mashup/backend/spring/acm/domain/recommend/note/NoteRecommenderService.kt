package mashup.backend.spring.acm.domain.recommend.note

import mashup.backend.spring.acm.domain.member.MemberDetailVo
import mashup.backend.spring.acm.domain.note.Note
import mashup.backend.spring.acm.domain.note.NoteSimpleVo
import mashup.backend.spring.acm.domain.recommend.RecommenderBuilder
import org.springframework.stereotype.Service

@Service
class NoteRecommenderService(
    recommendNotesByNoteGroupIdsService: RecommendNotesByNoteGroupIdsService,
    recommendNotesByDefaultService: RecommendNotesByDefaultService
) {
    private val notesByNoteGroupIdsRecommender = RecommenderBuilder<Note>()
        .recommendService(listOf(
            recommendNotesByNoteGroupIdsService,
            recommendNotesByDefaultService
        ))
        .build()
    fun recommendNotesByNoteGroupIds(memberDetailVo: MemberDetailVo, size: Int) = notesByNoteGroupIdsRecommender
        .recommend(memberDetailVo, size)
        .map { NoteSimpleVo(it) }
}