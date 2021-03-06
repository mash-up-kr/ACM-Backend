package mashup.backend.spring.acm.application.recommend

import mashup.backend.spring.acm.application.ApplicationService
import mashup.backend.spring.acm.application.brand.BrandCacheApplicationService
import mashup.backend.spring.acm.application.member.MemberApplicationService
import mashup.backend.spring.acm.domain.member.AgeGroup
import mashup.backend.spring.acm.domain.member.MemberDetailVo
import mashup.backend.spring.acm.domain.member.MemberStatus
import mashup.backend.spring.acm.domain.note.NoteGroupService
import mashup.backend.spring.acm.domain.perfume.PerfumeService
import mashup.backend.spring.acm.domain.perfume.PerfumeSimpleVo
import mashup.backend.spring.acm.domain.recommend.note.NoteRecommenderService
import mashup.backend.spring.acm.domain.recommend.perfume.PerfumeRecommenderCacheService
import mashup.backend.spring.acm.domain.recommend.perfume.PerfumeRecommenderService
import mashup.backend.spring.acm.presentation.api.recommend.*
import mashup.backend.spring.acm.presentation.assembler.toDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory

interface RecommendApplicationService {
    fun recommendSimilarPerfumes(perfumeId: Long): List<PerfumeSimpleVo>
    fun recommendMainPerfumes(memberId: Long): MainRecommend
}

@ApplicationService
class RecommendApplicationServiceImpl(
    private val memberApplicationService: MemberApplicationService,
    private val perfumeService: PerfumeService,
    private val perfumeRecommenderService: PerfumeRecommenderService,
    private val perfumeRecommenderCacheService: PerfumeRecommenderCacheService,
    private val noteRecommenderService: NoteRecommenderService,
    private val noteGroupService: NoteGroupService,
    private val brandCacheApplicationService: BrandCacheApplicationService,
) : RecommendApplicationService {
    override fun recommendSimilarPerfumes(perfumeId: Long): List<PerfumeSimpleVo> {
        log.debug("[RECOMMEND_PERFUMES][recommendSimilarPerfumes] perfumeId=$perfumeId")
        val perfume = perfumeService.getPerfume(perfumeId)
        val noteGroupIds = perfume.notes.mapNotNull { it.note.noteGroup?.id }.distinctBy { it }
        val memberForSimilarPerfumes = MemberDetailVo(
            id = -1L,
            status = MemberStatus.WITHDRAWAL,
            name = "memberForSimilarPerfumes",
            gender = perfume.gender.toMemberGender(),
            ageGroup = AgeGroup.UNKNOWN,
            noteGroupIds = noteGroupIds,
            noteGroupSimpleVoList = noteGroupService.getNoteGroupsByIdIn(noteGroupIds = noteGroupIds),
        )

        return perfumeRecommenderService.recommendSimilarPerfumes(
            memberForSimilarPerfumes,
            DEFAULT_RECOMMEND_SIMILAR_PERFUMES_COUNT
        )
    }

    override fun recommendMainPerfumes(memberId: Long): MainRecommend {
        val member = memberApplicationService.getMemberInfo(memberId)
        val title = if (!member.hasOnboard()) "????????? ?????? ??????" else "????????? ?????? ????????????"

        // ?????? ?????????
        val popularBrands = brandCacheApplicationService.getPopularBrands()

        // ?????? ?????? ??????
        val perfumesByOnboard = recommendPerfumesByOnboard(memberId, DEFAULT_MY_RECOMMEND_PERFUMES_COUNT)
        perfumesByOnboard.no = 1
        val perfumesByGender = recommendPerfumesByGender(memberId, DEFAULT_RECOMMEND_PERFUMES_COUNT)
        perfumesByGender.no = 2
        val popularPerfumes = recommendPopularPerfumes(memberId, DEFAULT_RECOMMEND_PERFUMES_COUNT)
        popularPerfumes.no = 3
        val perfumesByNoteGroup = recommendPerfumesByNoteGroup(memberId, DEFAULT_RECOMMEND_PERFUMES_COUNT)
        perfumesByNoteGroup.no = 4

        // ?????? ?????? ?????? ??????
        val noteGroups = recommendNoteGroupsByOnboard(memberId,
            DEFAULT_RECOMMEND_NOTE_GROUPS_COUNT,
            DEFAULT_RECOMMEND_NOTES_COUNT,
            DEFAULT_RECOMMEND_PERFUMES_COUNT,
        )

        return MainRecommend(
            hasOnboarded = member.hasOnboard(),
            title = title,
            popularBrands = popularBrands.map { it.toDto() },
            recommendPerfumes = listOf(
                perfumesByOnboard,
                perfumesByGender,
                popularPerfumes,
                perfumesByNoteGroup,
            ),
            recommendNoteGroups = noteGroups,
        )
    }

    private fun recommendPerfumesByOnboard(memberId: Long, size: Int): SimpleRecommendPerfumes {
        log.debug("[RECOMMEND_PERFUMES][recommendPerfumesByOnboard] memberId=$memberId, size:$size")
        val member = memberApplicationService.getMemberInfo(memberId)

        return SimpleRecommendPerfumes(
            title = "????????? ?????? ??????",
            perfumes = perfumeRecommenderService.recommendPerfumesByOnboard(member, size)
                .map { it.toDto() }
        )
    }

    private fun recommendPerfumesByGender(memberId: Long, size: Int): SimpleRecommendPerfumes {
        log.debug("[RECOMMEND_PERFUMES][recommendPerfumesByGender] memberId=$memberId, size:$size")
        val member = memberApplicationService.getMemberInfo(memberId)

        return SimpleRecommendPerfumes(
            title = if (!member.hasGender()) "????????? ??????" else "${member.gender.name}, ?????? ??????",
            perfumes = perfumeRecommenderCacheService.recommendPerfumesByGender(member, size)
                .map { it.toDto() }
        )
    }

    private fun recommendPopularPerfumes(memberId: Long, size: Int): SimpleRecommendPerfumes {
        log.debug("[RECOMMEND_PERFUMES][recommendPopularPerfumes] memberId=$memberId, size:$size")
        val member = memberApplicationService.getMemberInfo(memberId)

        return SimpleRecommendPerfumes(
            title = "?????? ???????????? ????????? ?????????!",
            perfumes = perfumeRecommenderCacheService.recommendPopularPerfumes(member, size)
                .map { it.toDto() }
        )
    }

    private fun recommendPerfumesByNoteGroup(memberId: Long, size: Int): SimpleRecommendPerfumes {
        log.debug("[RECOMMEND_PERFUMES][recommendPerfumesByNoteGroup] memberId=$memberId, size:$size")
        val member = memberApplicationService.getMemberInfo(memberId)
        val title = if (member.hasOnboard()) "{????????? ???}, ??? ?????? ??????????" else "???????????? ?????? ??????"

        return SimpleRecommendPerfumes(
            title = title,
            perfumes = perfumeRecommenderService.recommendPerfumesByNoteGroup(member, size)
                .map { it.toDto() }
        )
    }

    private fun recommendNoteGroupsByOnboard(
        memberId: Long,
        noteGroupSize: Int,
        noteSize: Int,
        perfumeSize: Int,
    ): List<RecommendNoteGroup> {
        log.debug("[RECOMMEND_PERFUMES][recommendNotesByOnboard] memberId=$memberId, noteSize:$noteSize, perfumeSize:$perfumeSize")
        val member = memberApplicationService.getMemberInfo(memberId)
        val noteGroupIds = member.noteGroupIds.toMutableList()
        if (noteGroupIds.size < noteGroupSize) {
            noteGroupService.getByRandom(noteGroupSize).forEach { noteGroupIds.add(it.id) }
            noteGroupIds.distinctBy { it }
        }
        if (noteGroupIds.size > noteGroupSize) {
            val removeCount = noteGroupIds.size - noteGroupSize
            for (idx in 0 until removeCount) {
                noteGroupIds.removeLast()
            }
        }

        val recommend4Members = mutableListOf<MemberDetailVo>()
        for (i in noteGroupIds.indices) {
            recommend4Members.add(
                MemberDetailVo(
                    id = member.id,
                    status = member.status,
                    name = member.name,
                    gender = member.gender,
                    ageGroup = member.ageGroup,
                    noteGroupIds = listOf(noteGroupIds[i]),
                    noteGroupSimpleVoList = member.noteGroupSimpleVoList,
                )
            )
        }

        return recommend4Members.map { recommend4Member ->
            val noteGroupId = recommend4Member.noteGroupIds[0]
            RecommendNoteGroup(
                id = noteGroupId,
                name = noteGroupService.getById(noteGroupId)!!.name,
                notes = noteRecommenderService.recommendNotesByNoteGroupIds(recommend4Member, noteSize)
                    .map { note ->
                        val perfumeSimpleVoSet = mutableSetOf<PerfumeSimpleVo>()
                        perfumeSimpleVoSet.addAll(perfumeService.getPerfumesByNoteId(note.id, perfumeSize)
                            .map { PerfumeSimpleVo(it) })
                        while (perfumeSimpleVoSet.size < 10) {
                            perfumeSimpleVoSet.addAll(
                                perfumeService.getPerfumesByNoteGroupId(noteGroupId, 10 - perfumeSimpleVoSet.size)
                            )
                        }
                        RecommendNote(
                            id = note.id,
                            name = note.name,
                            perfumes = perfumeSimpleVoSet.map { it.toDto() }
                        )
                    }
            )
        }
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(RecommendApplicationService::class.java)
        const val DEFAULT_RECOMMEND_PERFUMES_COUNT = 10
        const val DEFAULT_MY_RECOMMEND_PERFUMES_COUNT = 3
        const val DEFAULT_RECOMMEND_SIMILAR_PERFUMES_COUNT = 3
        const val DEFAULT_RECOMMEND_NOTES_COUNT = 3
        const val DEFAULT_RECOMMEND_NOTE_GROUPS_COUNT = 3
    }
}