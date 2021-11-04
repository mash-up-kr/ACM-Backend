package mashup.backend.spring.acm.application.recommend

import mashup.backend.spring.acm.application.ApplicationService
import mashup.backend.spring.acm.domain.member.MemberDetailVo
import mashup.backend.spring.acm.domain.note.NoteGroupService
import mashup.backend.spring.acm.domain.note.NoteSimpleVo
import mashup.backend.spring.acm.domain.perfume.PerfumeService
import mashup.backend.spring.acm.presentation.api.recommend.NOTE_GROUP_ORIGINAL_NAMES
import mashup.backend.spring.acm.presentation.api.recommend.RecommendNote
import mashup.backend.spring.acm.presentation.assembler.hasNoteGroupIds
import mashup.backend.spring.acm.presentation.assembler.toSimpleRecommendPerfume

abstract class NoteRecommendWithMemberApplicationService {
    fun recommendPerfumes(member: MemberDetailVo, recommendNotes: MutableList<RecommendNote>, size: Int): MutableList<RecommendNote> {
        if (recommendNotes.size >= size) return recommendNotes
        return process(member, recommendNotes, size).distinctBy { it.id }.toMutableList()
    }

    protected abstract fun process(member: MemberDetailVo, recommendNotes: MutableList<RecommendNote>, size: Int): MutableList<RecommendNote>
}

@ApplicationService
class OnboardNoteRecommendApplicationService(
    private val noteGroupService: NoteGroupService,
    private val perfumeService: PerfumeService
): NoteRecommendWithMemberApplicationService() {
    override fun process(
        member: MemberDetailVo,
        recommendNotes: MutableList<RecommendNote>,
        size: Int
    ): MutableList<RecommendNote> {
        if (!member.hasNoteGroupIds()) {
            return getDefaultNotes(recommendNotes, size)
        }
        val recommendNoteNames = recommendNotes.map { it.name }
        val notes = member.noteGroupIds.map { noteGroupService.getDetailById(it).notes }
            .flatten()
            .filter { !recommendNoteNames.contains(it.name) }
            .distinctBy { it.id }

        return getDefaultNotes(addRecommendNoteByNotes(notes, recommendNotes, size), size)
    }

    private fun getDefaultNotes(recommendNotes: MutableList<RecommendNote>, size: Int): MutableList<RecommendNote> {
        val recommendNoteNames = recommendNotes.map { it.name }
        val notes = NOTE_GROUP_ORIGINAL_NAMES.filter { !recommendNoteNames.contains(it) }
            .shuffled().subList(0, size - recommendNotes.size)
            .mapNotNull { noteGroupService.getNoteGroupByName(it)?.notes }
            .flatten()
            .distinctBy { it.id }
            .map { NoteSimpleVo(it) }

        return addRecommendNoteByNotes(notes, recommendNotes, size)
    }

    private fun addRecommendNoteByNotes(notes: List<NoteSimpleVo>, recommendNotes: MutableList<RecommendNote>, size: Int): MutableList<RecommendNote>  {
        for (note in notes) {
            if (recommendNotes.size >= size) break
            val perfumes = perfumeService.getPerfumesByNoteId(note.id, RecommendApplicationService.DEFAULT_RECOMMEND_PERFUMES_COUNT)
                .map { it.toSimpleRecommendPerfume() }
            recommendNotes.add(RecommendNote(id = note.id, name = note.name, recommendPerfumes = perfumes))
        }
        return recommendNotes
    }
}