package mashup.backend.spring.acm.domain.recommend.perfume

import mashup.backend.spring.acm.domain.note.NoteGroupService
import mashup.backend.spring.acm.domain.perfume.Perfume
import mashup.backend.spring.acm.domain.perfume.PerfumeService
import mashup.backend.spring.acm.domain.recommend.RecommendRequestVo
import org.springframework.stereotype.Service

@Service
class RecommendPerfumesByNoteGroupIdsAndGenderService(
    private val noteGroupService: NoteGroupService,
    private val perfumeService: PerfumeService
): RecommendPerfumesService {
    override fun supports(recommendRequestVo: RecommendRequestVo): Boolean {
        val member = recommendRequestVo.memberDetailVo ?: return false

        return member.hasNoteGroupIds() && member.hasGender()
    }

    override fun getItems(recommendRequestVo: RecommendRequestVo): List<Perfume> {
        // 노트그룹과 성별이 같은 향수중 랜덤
        val member = recommendRequestVo.memberDetailVo!!
        val size = recommendRequestVo.size
        val noteGroup = noteGroupService.getRecommendNoteGroupId(recommendRequestVo.exceptIds ?: emptySet(), recommendRequestVo.memberDetailVo.noteGroupIds)

        return perfumeService.getPerfumesByNoteIdAndGender(noteGroup.id, member.getPerfumeGender(), size)
    }
}