package mashup.backend.spring.acm.application.recommend

import mashup.backend.spring.acm.application.ApplicationService
import mashup.backend.spring.acm.domain.ResultCode
import mashup.backend.spring.acm.domain.exception.BusinessException
import mashup.backend.spring.acm.domain.member.MemberService
import mashup.backend.spring.acm.domain.note.NoteGroupDetailVo
import mashup.backend.spring.acm.domain.note.NoteGroupService
import mashup.backend.spring.acm.domain.perfume.PerfumeService
import mashup.backend.spring.acm.presentation.api.recommend.SAMPLE_RECOMMEND_PERFUMES
import mashup.backend.spring.acm.presentation.api.recommend.SimpleRecommendPerfume
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@ApplicationService
class RecommendApplicationService(
    private val memberService: MemberService,
    private val noteGroupService: NoteGroupService,
    private val perfumeService: PerfumeService
) {
    fun getOnboardPopularPerfumes(): List<SimpleRecommendPerfume> {
        val recommendPerfumes = mutableListOf<SimpleRecommendPerfume>()

        // TODO : 1. 최근 보관함에 많이 담은 향수
        var cnt = DEFAULT_RECOMMEND_COUNT - recommendPerfumes.size

        // 2. 인기많은 노트의 향수
        if (cnt > 0) {
            val noteGroup = getBestOnboardNoteGroup()

            for (note in noteGroup.notes) {
                if (recommendPerfumes.size >= DEFAULT_RECOMMEND_COUNT) break

                val perfumes = perfumeService.getPerfumesByNoteId(note.id, cnt).map { SimpleRecommendPerfume.from(it) }
                cnt -= perfumes.size
                perfumes.forEach { recommendPerfumes.add(it) }
            }
        }

        // 3. 위의 내용으로도 향수가 부족할 경우
        if (cnt > 0) {
            SAMPLE_RECOMMEND_PERFUMES.shuffled().subList(0, cnt).forEach { recommendPerfumes.add(it) }
        }

        return recommendPerfumes
    }

    // FIXME: 회원수 많아짐에 따라 배치 작업으로 돌려야 함
    fun getBestOnboardNoteGroup(): NoteGroupDetailVo {
        var maxCount = 0L
        var bestOnboardNoteGroupId = -1L
        val countMap = mutableMapOf<Long, Long>()

        val noteGroupIdsList = memberService.findAllMemberDetail().map { it.noteGroupIds }
        for (noteGroupIds in noteGroupIdsList) {
            noteGroupIds.forEach {
                if (countMap.containsKey(it)) {
                    countMap[it] = countMap[it]!!.plus(1)
                } else {
                    countMap[it] = 1
                }

                if (maxCount < countMap[it]!!) {
                    maxCount = countMap[it]!!
                    bestOnboardNoteGroupId = it
                }
            }
        }

        if (bestOnboardNoteGroupId == -1L) {
            log.error("온보딩 내용이 전혀 없는 경우 발생!")
            throw BusinessException(ResultCode.ONBOARD_DATA_NOT_EXIST, ResultCode.ONBOARD_DATA_NOT_EXIST.message)
        }

        return noteGroupService.getDetailById(bestOnboardNoteGroupId)
    }

    companion object {
        private const val DEFAULT_RECOMMEND_COUNT = 10
        val log: Logger = LoggerFactory.getLogger(RecommendApplicationService::class.java)
    }
}