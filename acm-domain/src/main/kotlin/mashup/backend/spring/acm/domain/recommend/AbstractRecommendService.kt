package mashup.backend.spring.acm.domain.recommend

import mashup.backend.spring.acm.domain.ResultCode
import mashup.backend.spring.acm.domain.exception.BusinessException
import mashup.backend.spring.acm.domain.note.NoteGroupDetailVo
import mashup.backend.spring.acm.domain.note.NoteGroupService
import mashup.backend.spring.acm.domain.perfume.PerfumeService

abstract class AbstractRecommendService(
    private val noteGroupService: NoteGroupService,
    private val perfumeService: PerfumeService
) {
    /**
     * 해당 메소드를 사용할 때 noteGroupIds가 null이나 empty가 아닌지 검증하고 사용해야한다.
     */
    protected fun getRecommendNoteGroupId(perfumeIds: Set<Long>, noteGroupIds: List<Long>): NoteGroupDetailVo {
        if (noteGroupIds.isNullOrEmpty())
            throw BusinessException(ResultCode.INTERNAL_SERVER_ERROR, "서버에서 잘못된 요청을 허용했습니다. noteGroupIds: $noteGroupIds")
        if (perfumeIds.isNullOrEmpty()) return noteGroupService.getDetailById(noteGroupIds.shuffled()[0])

        var perfumeNoteGroupIds = perfumeService.getPerfume(perfumeIds.elementAt(0)).notes
            .mapNotNull { it.note.noteGroup?.id }
            .toSet()
        if (perfumeIds.size > 1)  {
            var unionNoteGroupIds = perfumeNoteGroupIds
            for (index in 1 until perfumeIds.size) {
                unionNoteGroupIds = unionNoteGroupIds.filter {
                    perfumeService.getPerfume(perfumeIds.elementAt(0)).notes
                        .mapNotNull { it.note.noteGroup?.id }
                        .toSet()
                        .contains(index.toLong())
                }
                .toSet()
            }
            if (unionNoteGroupIds.isNotEmpty()) perfumeNoteGroupIds = unionNoteGroupIds
        }

        val unionNoteGroupIds = perfumeNoteGroupIds.filter {
            noteGroupIds.contains(it)
        }.toSet()
        if (unionNoteGroupIds.isNotEmpty()) {
            return noteGroupService.getDetailById(unionNoteGroupIds.elementAt(0))
        }

        return noteGroupService.getDetailById(perfumeNoteGroupIds.elementAt(0))
    }}