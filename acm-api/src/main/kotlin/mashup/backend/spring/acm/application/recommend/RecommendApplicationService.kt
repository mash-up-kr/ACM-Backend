package mashup.backend.spring.acm.application.recommend

import mashup.backend.spring.acm.application.ApplicationService
import mashup.backend.spring.acm.application.brand.BrandApplicationService
import mashup.backend.spring.acm.application.member.MemberApplicationService
import mashup.backend.spring.acm.domain.perfume.PerfumeService
import mashup.backend.spring.acm.domain.perfume.PerfumeSimpleVo
import mashup.backend.spring.acm.domain.recommend.note.NoteRecommenderService
import mashup.backend.spring.acm.domain.recommend.perfume.PerfumeRecommenderService
import mashup.backend.spring.acm.presentation.api.recommend.MainRecommend
import mashup.backend.spring.acm.presentation.api.recommend.RecommendNote
import mashup.backend.spring.acm.presentation.api.recommend.SimpleRecommendPerfumes
import mashup.backend.spring.acm.presentation.assembler.toSimpleRecommendPerfume
import org.slf4j.Logger
import org.slf4j.LoggerFactory

interface RecommendApplicationService {
    fun recommendMainPerfumes(memberId: Long): MainRecommend
}

@ApplicationService
class RecommendApplicationServiceImpl(
    private val memberApplicationService: MemberApplicationService,
    private val perfumeService: PerfumeService,
    private val perfumeRecommenderService: PerfumeRecommenderService,
    private val noteRecommenderService: NoteRecommenderService,
    private val brandApplicationService: BrandApplicationService
): RecommendApplicationService {

    override fun recommendMainPerfumes(memberId: Long): MainRecommend {
        val member = memberApplicationService.getMemberInfo(memberId)
        val title = if (!member.hasOnboard()) "디깅의 추천 향수" else "당신을 위한 추천향수"

        // 추천 브랜드
        val popularBrands = brandApplicationService.getPopularBrands()

        // 추천 향수들
        val perfumesByOnboard = recommendPerfumesByOnboard(memberId, DEFAULT_MY_RECOMMEND_PERFUMES_COUNT)
        perfumesByOnboard.no = 1
        val perfumesByGender = recommendPerfumesByGender(memberId, DEFAULT_RECOMMEND_PERFUMES_COUNT)
        perfumesByGender.no = 2
        val popularPerfumes = recommendPopularPerfumes(memberId, DEFAULT_RECOMMEND_PERFUMES_COUNT)
        popularPerfumes.no = 3
        val perfumesByNoteGroup = recommendPerfumesByNoteGroup(memberId, DEFAULT_RECOMMEND_PERFUMES_COUNT)
        popularPerfumes.no = 4

        // 추천 노트들
        val notes = recommendNotesByOnboard(memberId, DEFAULT_RECOMMEND_NOTES_COUNT, DEFAULT_RECOMMEND_PERFUMES_COUNT)

        return MainRecommend(
            title = title,
            popularBrands = popularBrands,
            recommendPerfumeList = listOf(
                perfumesByOnboard,
                perfumesByGender,
                popularPerfumes,
                perfumesByNoteGroup
            ),
            recommendNotes = notes
        )
    }

    private fun recommendPerfumesByOnboard(memberId: Long, size: Int): SimpleRecommendPerfumes {
        log.info("[RECOMMEND_PERFUMES][recommendPerfumesByOnboard] memberId=$memberId, size:$size")
        val member = memberApplicationService.getMemberInfo(memberId)

        return SimpleRecommendPerfumes(
            title = "온보딩 맞춤 향수",
            perfumes = perfumeRecommenderService.recommendPerfumesByOnboard(member, size)
                .map { it.toSimpleRecommendPerfume() }
        )
    }

    private fun recommendPerfumesByGender(memberId: Long, size: Int): SimpleRecommendPerfumes {
        log.info("[RECOMMEND_PERFUMES][recommendPerfumesByGender] memberId=$memberId, size:$size")
        val member = memberApplicationService.getMemberInfo(memberId)

        return SimpleRecommendPerfumes(
            title = if (!member.hasGender()) "이달의 향수" else "${member.gender.name}, 인기 향수",
            perfumes = perfumeRecommenderService.recommendPerfumesByGender(member, size)
                .map { it.toSimpleRecommendPerfume() }
        )
    }

    private fun recommendPopularPerfumes(memberId: Long, size: Int): SimpleRecommendPerfumes {
        log.info("[RECOMMEND_PERFUMES][recommendPopularPerfumes] memberId=$memberId, size:$size")
        val member = memberApplicationService.getMemberInfo(memberId)

        return SimpleRecommendPerfumes(
            title = "모든 분들에게 인기가 많아요!",
            perfumes = perfumeRecommenderService.recommendPopularPerfumes(member, size)
                .map { it.toSimpleRecommendPerfume() }
       )
    }

    private fun recommendPerfumesByNoteGroup(memberId: Long, size: Int): SimpleRecommendPerfumes {
        log.info("[RECOMMEND_PERFUMES][recommendPerfumesByNoteGroup] memberId=$memberId, size:$size")
        val member = memberApplicationService.getMemberInfo(memberId)
        val hasGroupIds =  member.hasNoteGroupIds()
        val title = if (hasGroupIds) "취향을 맞춘 노트" else "내가 좋아할 노트"

        return SimpleRecommendPerfumes(
            title = title,
            perfumes = perfumeRecommenderService.recommendPerfumesByNoteGroup(member, size)
                .map { it.toSimpleRecommendPerfume() }
        )
    }

    private fun recommendNotesByOnboard(memberId: Long, noteSize: Int, perfumeSize: Int): List<RecommendNote> {
        log.info("[RECOMMEND_PERFUMES][recommendNotesByOnboard] memberId=$memberId, noteSize:$noteSize, perfumeSize:$perfumeSize")
        val member = memberApplicationService.getMemberInfo(memberId)
        return noteRecommenderService.recommendNotesByNoteGroupIds(member, noteSize)
            .map { note ->
                RecommendNote(
                    id = note.id,
                    name = note.name,
                    recommendPerfumes = perfumeService.getPerfumesByNoteId(note.id, perfumeSize)
                        .map { perfume ->  PerfumeSimpleVo(perfume).toSimpleRecommendPerfume() }
                )
            }
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(RecommendApplicationService::class.java)
        const val DEFAULT_RECOMMEND_PERFUMES_COUNT = 10
        const val DEFAULT_MY_RECOMMEND_PERFUMES_COUNT = 3
        const val DEFAULT_RECOMMEND_NOTES_COUNT = 3
    }
}