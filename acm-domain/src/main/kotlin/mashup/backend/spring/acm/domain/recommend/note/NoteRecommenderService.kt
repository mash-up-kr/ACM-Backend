package mashup.backend.spring.acm.domain.recommend.note

import mashup.backend.spring.acm.domain.member.MemberDetailVo
import mashup.backend.spring.acm.domain.note.Note
import mashup.backend.spring.acm.domain.note.NoteSimpleVo
import mashup.backend.spring.acm.domain.recommend.Recommender
import org.springframework.stereotype.Service

@Service
class NoteRecommenderService(
    private val notesByNoteGroupIdsRecommender: Recommender<Note>
) {
    fun recommendNotesByNoteGroupIds(memberDetailVo: MemberDetailVo, size: Int) = notesByNoteGroupIdsRecommender
        .recommend(memberDetailVo, size)
        .map { NoteSimpleVo(it) }
}