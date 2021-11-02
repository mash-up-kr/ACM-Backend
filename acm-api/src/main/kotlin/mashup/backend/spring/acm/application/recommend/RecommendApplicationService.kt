package mashup.backend.spring.acm.application.recommend

import mashup.backend.spring.acm.application.ApplicationService
import mashup.backend.spring.acm.domain.ResultCode
import mashup.backend.spring.acm.domain.exception.BusinessException
import mashup.backend.spring.acm.domain.member.MemberService
import mashup.backend.spring.acm.domain.note.NoteGroupDetailVo
import mashup.backend.spring.acm.domain.note.NoteGroupService
import mashup.backend.spring.acm.domain.perfume.Gender
import mashup.backend.spring.acm.domain.perfume.PerfumeService
import mashup.backend.spring.acm.presentation.api.recommend.SAMPLE_RECOMMEND_PERFUMES
import mashup.backend.spring.acm.presentation.api.recommend.SimpleRecommendPerfume
import mashup.backend.spring.acm.presentation.assembler.getPerfumeGender
import mashup.backend.spring.acm.presentation.assembler.hasOnboard
import mashup.backend.spring.acm.presentation.assembler.toSimpleRecommendPerfume
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@ApplicationService
class RecommendApplicationService(
    private val memberService: MemberService,
    private val noteGroupService: NoteGroupService,
    private val perfumeService: PerfumeService
) {
    fun getMyRecommendPerfumes(memberId: Long): List<SimpleRecommendPerfume> {
        val member = memberService.findDetailById(memberId)

        // 온보딩이 존재 하지 않은 경우 인기 향수에서 랜덤 3개
        if (!member!!.hasOnboard()) {
            return getOnboardPopularPerfumes().shuffled().subList(0, DEFAULT_MY_RECOMMEND_COUNT)
        }

        // 온보딩 3개(나이대,노트 선택,성별)
        val myRecommendPerfumes = mutableListOf<SimpleRecommendPerfume>()
        var cnt = DEFAULT_MY_RECOMMEND_COUNT

        // FIXME : 1. 온보딩 기반으로 컬렉션 조회

        // 2. 온보딩 기반으로 향수 검색
        if (cnt > 0) {
            // 온보딩(노트, 성별) 기반으로 향수 검색
            getMyRecommendPerfumesByNoteGroupAndGender(member.noteGroupIds, member.getPerfumeGender(), cnt ).forEach {
                myRecommendPerfumes.add(it)
                cnt--
            }
        }

        // 3. 디폴트 추천
        if (cnt > 0) {
            getOnboardPopularPerfumes().shuffled().subList(0, cnt).forEach { myRecommendPerfumes.add(it) }
        }

        return myRecommendPerfumes
    }

    private fun getMyRecommendPerfumesByNoteGroupAndGender(noteGroupIds: List<Long>, gender: Gender, size: Int): List<SimpleRecommendPerfume> {
        val myRecommendPerfumes = mutableListOf<SimpleRecommendPerfume>()
        // 온보딩에서 선택한 노트그룹중 랜덤 1개
        val notes = noteGroupService.getDetailById(noteGroupIds.shuffled().get(0)).notes.shuffled()
        var count = size

        // 노트그룹의 노트 개수가 count 이상이면, 노트당 향수 하나씩 추천
        if (notes.size > count) {
            notes.subList(0, count).forEach { note ->
                perfumeService.getPerfumesByNoteIdAndGender(note.id, gender, 1).forEach { perfume ->
                    myRecommendPerfumes.add(perfume.toSimpleRecommendPerfume())
                    count--
                }
            }

            return myRecommendPerfumes
        }

        // 노트그룹의 노트 개수가 count 미만이면, 하나의 노트에서 count 만큼 향수 추천
        perfumeService.getPerfumesByNoteIdAndGender(notes[0].id, gender, count).forEach {
            myRecommendPerfumes.add(it.toSimpleRecommendPerfume())
            count--
        }

        return myRecommendPerfumes
    }

    fun getOnboardPopularPerfumes(): List<SimpleRecommendPerfume> {
        val recommendPerfumes = mutableListOf<SimpleRecommendPerfume>()

        // TODO : 1. 최근 보관함에 많이 담은 향수
        var cnt = DEFAULT_RECOMMEND_COUNT - recommendPerfumes.size

        // 2. 인기많은 노트의 향수
        if (cnt > 0) {
            val noteGroup = getBestOnboardNoteGroup()

            for (note in noteGroup.notes) {
                if (recommendPerfumes.size >= DEFAULT_RECOMMEND_COUNT) break

                val perfumes = perfumeService.getPerfumesByNoteId(note.id, cnt).map { it.toSimpleRecommendPerfume() }
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
        private const val DEFAULT_MY_RECOMMEND_COUNT = 3
        val log: Logger = LoggerFactory.getLogger(RecommendApplicationService::class.java)
    }
}