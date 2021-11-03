package mashup.backend.spring.acm.application.recommend

import mashup.backend.spring.acm.application.ApplicationService
import mashup.backend.spring.acm.domain.member.MemberService
import mashup.backend.spring.acm.domain.note.NoteGroupService
import mashup.backend.spring.acm.domain.perfume.Gender
import mashup.backend.spring.acm.domain.perfume.PerfumeService
import mashup.backend.spring.acm.presentation.api.recommend.SimpleRecommendPerfume
import mashup.backend.spring.acm.presentation.api.recommend.SimpleRecommendPerfumes
import mashup.backend.spring.acm.presentation.assembler.getPerfumeGender
import mashup.backend.spring.acm.presentation.assembler.hasGender
import mashup.backend.spring.acm.presentation.assembler.hasOnboard
import mashup.backend.spring.acm.presentation.assembler.toSimpleRecommendPerfume
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@ApplicationService
class RecommendApplicationService(
    private val myOnboardPerfumesWithCollectionRecommendApplicationService: MyOnboardPerfumesWithCollectionRecommendApplicationService,
    private val myOnboardPerfumesRecommendApplicationService: MyOnboardPerfumesRecommendApplicationService,
    private val genderPerfumesWithOnboardRecommendApplicationService: GenderPerfumesWithOnboardRecommendApplicationService,
    private val genderPerfumesRecommendApplicationService: GenderPerfumesRecommendApplicationService,
    private val recentCollectionPerfumesRecommendApplicationService: RecentCollectionPerfumesRecommendApplicationService,
    private val samplePerfumesRecommendApplicationService: SamplePerfumesRecommendApplicationService,
    private val popularNotePerfumesRecommendApplicationService: PopularNotePerfumesRecommendApplicationService,
    private val monthlyPerfumesRecommendApplicationService: MonthlyPerfumesRecommendApplicationService,
    private val memberService: MemberService
) {
    fun getMyRecommendPerfumes(memberId: Long): SimpleRecommendPerfumes {
        val member = memberService.findDetailById(memberId)

        // 온보딩이 존재 하지 않은 경우 인기 향수에서 랜덤 3개
        if (!member!!.hasOnboard()) {
            return SimpleRecommendPerfumes(
                title = "온보딩 추천 향수(인기향수에서 랜덤 3개)",
                perfumes = getPopularPerfumes().perfumes.shuffled().subList(0, DEFAULT_MY_RECOMMEND_COUNT)
            )
        }

        // 온보딩이 있을 경우 3개(나이대,노트 선택,성별)
        var myRecommendPerfumes = mutableListOf<SimpleRecommendPerfume>()

        // 1. 온보딩 기반으로 다른 사람들의 컬렉션에 담긴 향수 찾기
        myRecommendPerfumes = myOnboardPerfumesWithCollectionRecommendApplicationService.recommendPerfumes(member, myRecommendPerfumes, DEFAULT_MY_RECOMMEND_COUNT)
        // 2. 온보딩 기반으로 비슷한 향수 검색
        myRecommendPerfumes = myOnboardPerfumesRecommendApplicationService.recommendPerfumes(member, myRecommendPerfumes, DEFAULT_MY_RECOMMEND_COUNT)
        // 3. 디폴트 추천
        myRecommendPerfumes = samplePerfumesRecommendApplicationService.recommendPerfumes(myRecommendPerfumes, DEFAULT_MY_RECOMMEND_COUNT)

        return SimpleRecommendPerfumes(title = "온보딩 추천 향수", perfumes = myRecommendPerfumes)
    }

    fun getGenderRecommendPerfumes(memberId: Long): SimpleRecommendPerfumes {
        val member = memberService.findDetailById(memberId)
        var genderRecommendPerfumes = mutableListOf<SimpleRecommendPerfume>()

        // 온보딩이 존재 하지 않은 경우 이 달의 향수
        if (!member!!.hasGender()) {
            return SimpleRecommendPerfumes(
                title = "이달의 향수",
                perfumes = monthlyPerfumesRecommendApplicationService.recommendPerfumes(genderRecommendPerfumes, DEFAULT_RECOMMEND_COUNT)
            )
        }

        // 1. 같은 gender 들의 온보딩 노트 + 컬렉션 노트 중 가장 많이 선택한 노트 + 1) 해당 노트 중 컬렉션에 가장 많이 담긴 향수 or 2) 랜덤 추천
        genderRecommendPerfumes = genderPerfumesWithOnboardRecommendApplicationService.recommendPerfumes(member, genderRecommendPerfumes, DEFAULT_RECOMMEND_COUNT)
        // 2. 같은 gender 향수 랜덤 추천
        genderRecommendPerfumes = genderPerfumesRecommendApplicationService.recommendPerfumes(member, genderRecommendPerfumes, DEFAULT_RECOMMEND_COUNT)
        // 3. 샘플에서 추천
        genderRecommendPerfumes = samplePerfumesRecommendApplicationService.recommendPerfumes(genderRecommendPerfumes, DEFAULT_RECOMMEND_COUNT)

        return SimpleRecommendPerfumes(title = "온보딩 추천 향수", metaData = member.gender!!.name, perfumes = genderRecommendPerfumes)
    }


    fun getPopularPerfumes(): SimpleRecommendPerfumes {
        var recommendPerfumes = mutableListOf<SimpleRecommendPerfume>()

        // 1. 최근 보관함에 많이 담은 향수
        recommendPerfumes = recentCollectionPerfumesRecommendApplicationService.recommendPerfumes(recommendPerfumes, DEFAULT_RECOMMEND_COUNT)
        // 2. 인기많은 노트의 향수
        recommendPerfumes = popularNotePerfumesRecommendApplicationService.recommendPerfumes(recommendPerfumes, DEFAULT_RECOMMEND_COUNT)
        // 3. 샘플에서 추천
        recommendPerfumes = samplePerfumesRecommendApplicationService.recommendPerfumes(recommendPerfumes, DEFAULT_RECOMMEND_COUNT)


        return SimpleRecommendPerfumes(title = "03. 인기 향수", perfumes = recommendPerfumes)
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(RecommendApplicationService::class.java)
        const val DEFAULT_RECOMMEND_COUNT = 10
        const val DEFAULT_MY_RECOMMEND_COUNT = 3
    }
}