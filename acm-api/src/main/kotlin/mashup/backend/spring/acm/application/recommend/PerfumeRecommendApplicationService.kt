package mashup.backend.spring.acm.application.recommend

import mashup.backend.spring.acm.application.ApplicationService
import mashup.backend.spring.acm.domain.ResultCode
import mashup.backend.spring.acm.domain.exception.BusinessException
import mashup.backend.spring.acm.domain.member.MemberDetailVo
import mashup.backend.spring.acm.domain.member.MemberService
import mashup.backend.spring.acm.domain.note.NoteGroupDetailVo
import mashup.backend.spring.acm.domain.note.NoteGroupService
import mashup.backend.spring.acm.domain.perfume.Gender
import mashup.backend.spring.acm.domain.perfume.PerfumeService
import mashup.backend.spring.acm.presentation.api.recommend.SAMPLE_RECOMMEND_PERFUMES
import mashup.backend.spring.acm.presentation.api.recommend.SimpleRecommendPerfume
import mashup.backend.spring.acm.presentation.assembler.getPerfumeGender
import mashup.backend.spring.acm.presentation.assembler.hasNoteGroupIds
import mashup.backend.spring.acm.presentation.assembler.toSimpleRecommendPerfume

abstract class PerfumeRecommendApplicationService {
    fun recommendPerfumes(perfumes: MutableList<SimpleRecommendPerfume>, size: Int): MutableList<SimpleRecommendPerfume> {
        if (perfumes.size >= size) return perfumes
        return process(perfumes, size).distinctBy { it.id }.toMutableList()
    }

    protected abstract fun process(perfumes: MutableList<SimpleRecommendPerfume>, size: Int): MutableList<SimpleRecommendPerfume>
}

abstract class PerfumeRecommendWithMemberApplicationService {
    fun recommendPerfumes(member: MemberDetailVo, perfumes: MutableList<SimpleRecommendPerfume>, size: Int): MutableList<SimpleRecommendPerfume> {
        if (perfumes.size >= size) return perfumes
        return process(member, perfumes, size).distinctBy { it.id }.toMutableList()
    }

    protected abstract fun process(member: MemberDetailVo, perfumes: MutableList<SimpleRecommendPerfume>, size: Int): MutableList<SimpleRecommendPerfume>
}

@ApplicationService
class MyOnboardPerfumesWithDiggingRecommendApplicationService(
): PerfumeRecommendWithMemberApplicationService() {
    override fun process(
        member: MemberDetailVo,
        perfumes: MutableList<SimpleRecommendPerfume>,
        size: Int
    ): MutableList<SimpleRecommendPerfume> {
        // TODO : v2 개발에 따라 구현해야합니다.
        return perfumes
    }
}

@ApplicationService
class MyOnboardPerfumesRecommendApplicationService(
    private val perfumeService: PerfumeService,
    private val noteGroupService: NoteGroupService
): PerfumeRecommendWithMemberApplicationService() {
    override fun process(
        member: MemberDetailVo,
        perfumes: MutableList<SimpleRecommendPerfume>,
        size: Int
    ): MutableList<SimpleRecommendPerfume> {
        if (!member.hasNoteGroupIds()) return perfumes
        return getMyRecommendPerfumesByNoteGroupAndGender(perfumes, getNoteGroupId(perfumes, member.noteGroupIds), member.getPerfumeGender(), size - perfumes.size)
    }

    fun getNoteGroupId(perfumes: List<SimpleRecommendPerfume>, noteGroupIds: List<Long>): NoteGroupDetailVo {
        if (perfumes.isNullOrEmpty()) {
            return noteGroupService.getDetailById(noteGroupIds.shuffled()[0])
        }
        if (perfumes.size == 1) {
            val perfumeNoteGroupIds = perfumeService.getPerfume(perfumes[0].id).notes.mapNotNull { it.note.noteGroup?.id }
            val sameNoteGroupIds = perfumeNoteGroupIds.filter { noteGroupIds.contains(it) }
            if (sameNoteGroupIds.isNullOrEmpty()) return noteGroupService.getDetailById(perfumeNoteGroupIds[0])
        }
        val perfumeNoteGroupIdsList = perfumes.map { perfume -> perfumeService.getPerfume(perfume.id).notes.mapNotNull { it.note.noteGroup?.id } }
        var sameNoteGroupIds = perfumeNoteGroupIdsList[0]
        for (index in 1 until perfumeNoteGroupIdsList.size) {
            sameNoteGroupIds = sameNoteGroupIds.filter { perfumeNoteGroupIdsList[index].contains(it) }
        }

        return noteGroupService.getDetailById(sameNoteGroupIds.shuffled()[0])
    }

    private fun getMyRecommendPerfumesByNoteGroupAndGender(myRecommendPerfumes: MutableList<SimpleRecommendPerfume>, noteGroup: NoteGroupDetailVo, gender: Gender, size: Int): MutableList<SimpleRecommendPerfume> {
        val notes = noteGroup.notes.shuffled()

        // 노트그룹의 노트 개수가 count 이상이면, 노트당 향수 하나씩 추천
        if (notes.size > size) {
            notes.subList(0, size - myRecommendPerfumes.size).forEach { note ->
                perfumeService.getPerfumesByNoteIdAndGender(note.id, gender, 1).forEach { perfume ->
                    myRecommendPerfumes.add(perfume.toSimpleRecommendPerfume())
                }
            }

            return myRecommendPerfumes
        }

        // 노트그룹의 노트 개수가 count 미만이면, 하나의 노트에서 count 만큼 향수 추천
        perfumeService.getPerfumesByNoteIdAndGender(notes[0].id, gender, size - myRecommendPerfumes.size).forEach {
            myRecommendPerfumes.add(it.toSimpleRecommendPerfume())
        }

        return myRecommendPerfumes
    }
}

@ApplicationService
class GenderPerfumesWithOnboardRecommendApplicationService(
): PerfumeRecommendWithMemberApplicationService() {
    override fun process(
        member: MemberDetailVo,
        perfumes: MutableList<SimpleRecommendPerfume>,
        size: Int
    ): MutableList<SimpleRecommendPerfume> {
        // TODO : v2 개발에 따라 구현해야합니다.
        return perfumes
    }
}

@ApplicationService
class GenderPerfumesRecommendApplicationService(
    private val perfumeService: PerfumeService
): PerfumeRecommendWithMemberApplicationService() {
    override fun process(
        member: MemberDetailVo,
        perfumes: MutableList<SimpleRecommendPerfume>,
        size: Int
    ): MutableList<SimpleRecommendPerfume> {
        perfumeService.getPerfumesByGenderWithRandom(member.getPerfumeGender(), size - perfumes.size)
            .forEach { perfumes.add(it.toSimpleRecommendPerfume()) }
        return perfumes
    }
}

@ApplicationService
class RecentDiggingPerfumesRecommendApplicationService(
): PerfumeRecommendApplicationService() {
    override fun process(perfumes: MutableList<SimpleRecommendPerfume>, size: Int): MutableList<SimpleRecommendPerfume> {
        // TODO : v2 개발에 따라 구현해야합니다.
        return perfumes
    }
}

@ApplicationService
class PopularNotePerfumesRecommendApplicationService(
    private val memberService: MemberService,
    private val noteGroupService: NoteGroupService,
    private val perfumeService: PerfumeService
): PerfumeRecommendApplicationService() {
    override fun process(perfumes: MutableList<SimpleRecommendPerfume>, size: Int): MutableList<SimpleRecommendPerfume> {
        val noteGroup = getBestOnboardNoteGroup()

        for (note in noteGroup.notes) {
            if (perfumes.size >= size) break

            perfumeService.getPerfumesByNoteId(note.id, size - perfumes.size).map { it.toSimpleRecommendPerfume() }
                .forEach { perfumes.add(it) }
        }

        return perfumes
    }

    private fun getBestOnboardNoteGroup(): NoteGroupDetailVo {
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
            RecommendApplicationService.log.error("온보딩 내용이 전혀 없는 경우 발생!")
            throw BusinessException(ResultCode.ONBOARD_DATA_NOT_EXIST, ResultCode.ONBOARD_DATA_NOT_EXIST.message)
        }

        return noteGroupService.getDetailById(bestOnboardNoteGroupId)
    }
}

@ApplicationService
class MonthlyPerfumesRecommendApplicationService(
): PerfumeRecommendApplicationService() {
    override fun process(perfumes: MutableList<SimpleRecommendPerfume>, size: Int): MutableList<SimpleRecommendPerfume> {
        // TODO : 이 달의 향수 선정해야 함.
        SAMPLE_RECOMMEND_PERFUMES.shuffled().subList(0, size - perfumes.size).forEach { perfumes.add(it) }
        return perfumes
    }
}

@ApplicationService
class PresentPerfumesRecommendApplicationService(
): PerfumeRecommendApplicationService() {
    override fun process(perfumes: MutableList<SimpleRecommendPerfume>, size: Int): MutableList<SimpleRecommendPerfume> {
        // TODO : 선물하기 좋은 향수 선정해야 함.
        SAMPLE_RECOMMEND_PERFUMES.shuffled().subList(0, size - perfumes.size).forEach { perfumes.add(it) }
        return perfumes
    }
}