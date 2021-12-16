package mashup.backend.spring.acm.domain.recommend.perfume

import mashup.backend.spring.acm.domain.perfume.Perfume
import mashup.backend.spring.acm.domain.perfume.PerfumeService
import mashup.backend.spring.acm.domain.recommend.RecommendRequestVo
import mashup.backend.spring.acm.domain.recommend.notegroup.NoteGroupRecommenderSupportService
import org.springframework.stereotype.Service

@Service
class RecommendPerfumesByNoteGroupIdsAndGenderService(
    private val noteGroupRecommenderSupportService: NoteGroupRecommenderSupportService,
    private val perfumeService: PerfumeService,
) : RecommendPerfumesService {
    override fun supports(recommendRequestVo: RecommendRequestVo): Boolean {
        val memberDetailVo = recommendRequestVo.memberDetailVo ?: return false
        return memberDetailVo.hasNoteGroupIds()
    }

    /**
     * 노트 그룹이나 성별이 같은 향수
     * 노트 그룹이 없으면 향수 추천 안함
     */
    override fun getItems(recommendRequestVo: RecommendRequestVo): List<Perfume> {
        val member = recommendRequestVo.memberDetailVo!!
        val size = recommendRequestVo.size
        val noteGroupIds = recommendRequestVo.memberDetailVo.noteGroupIds
        if (noteGroupIds.isEmpty()) {
            return emptyList()
        }
        val noteGroup = noteGroupRecommenderSupportService.getRecommendNoteGroupId(
            perfumeIds = recommendRequestVo.exceptIds ?: emptySet(),
            noteGroupIds = recommendRequestVo.memberDetailVo.noteGroupIds,
        )
        return perfumeService.getPerfumesByNoteGroupIdAndGender(noteGroup.id, member.getPerfumeGender(), size)
    }
}