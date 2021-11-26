package mashup.backend.spring.acm.infrastructure.scheduler.perfume

import mashup.backend.spring.acm.application.recommend.RecommendApplicationServiceImpl.Companion.DEFAULT_RECOMMEND_PERFUMES_COUNT
import mashup.backend.spring.acm.domain.member.AgeGroup
import mashup.backend.spring.acm.domain.member.MemberDetailVo
import mashup.backend.spring.acm.domain.member.MemberGender
import mashup.backend.spring.acm.domain.member.MemberStatus
import mashup.backend.spring.acm.domain.note.NoteService
import mashup.backend.spring.acm.domain.perfume.PerfumeService
import mashup.backend.spring.acm.domain.recommend.perfume.PerfumeRecommenderService
import mashup.backend.spring.acm.infrastructure.scheduler.Scheduler
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class PerfumeScheduler(
    private val noteService: NoteService,
    private val perfumeService: PerfumeService,
    private val perfumeRecommendService: PerfumeRecommenderService
): Scheduler {
    override fun init() {
        perfumesByNoteId()
        recommendPopularPerfumes()
        recommendPerfumesByGender()
    }

    @Scheduled(cron = "0 0 2 * * MON")
    fun perfumesByNoteId() {
        log.info("[PERFUME_SCHEDULER] perfumesByNoteId >> start")
        val notes = noteService.getAllNotes()
        for (note in notes) {
            perfumeService.getPerfumesByNoteId(note.id, DEFAULT_RECOMMEND_PERFUMES_COUNT)
        }
        log.info("[PERFUME_SCHEDULER] perfumesByNoteId << end")
    }


    @Scheduled(cron = "0 0 3 * * *")
    fun recommendPopularPerfumes() {
        log.info("[PERFUME_SCHEDULER] recommendPopularPerfumes >> start")
        perfumeRecommendService.cachePutRecommendPopularPerfumes(
            createMockMemberDetailVoByGender(MemberGender.UNKNOWN),
            DEFAULT_RECOMMEND_PERFUMES_COUNT
        )
        log.info("[PERFUME_SCHEDULER] recommendPopularPerfumes << end")
    }

    @Scheduled(cron = "0 0 4 * * *")
    fun recommendPerfumesByGender() {
        log.info("[PERFUME_SCHEDULER] recommendPerfumesByGender >> start")
        listOf(
            createMockMemberDetailVoByGender(MemberGender.MALE),
            createMockMemberDetailVoByGender(MemberGender.FEMALE),
            createMockMemberDetailVoByGender(MemberGender.UNKNOWN)
        ).forEach { perfumeRecommendService.cachePutRecommendPerfumesByGender(it, DEFAULT_RECOMMEND_PERFUMES_COUNT) }
        log.info("[PERFUME_SCHEDULER] recommendPerfumesByGender << end")

    }

    private fun createMockMemberDetailVoByGender(memberGender: MemberGender): MemberDetailVo {
        return MemberDetailVo(
            id = -1,
            status = MemberStatus.ACTIVE,
            name = "MOCK",
            gender = memberGender,
            ageGroup = AgeGroup.UNKNOWN,
            noteGroupIds = emptyList(),
            noteGroupSimpleVoList = emptyList(),
        )
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(PerfumeScheduler::class.java)
    }
}