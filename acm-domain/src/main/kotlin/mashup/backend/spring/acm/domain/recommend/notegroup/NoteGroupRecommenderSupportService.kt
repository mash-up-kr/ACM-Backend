package mashup.backend.spring.acm.domain.recommend.notegroup

import mashup.backend.spring.acm.domain.exception.PerfumeNotFoundException
import mashup.backend.spring.acm.domain.note.NoteGroupDetailVo
import mashup.backend.spring.acm.domain.note.NoteGroupService
import mashup.backend.spring.acm.domain.perfume.PerfumeRepository
import org.springframework.stereotype.Service

@Service
class NoteGroupRecommenderSupportService(
    private val noteGroupService: NoteGroupService,
    private val perfumeRepository: PerfumeRepository,
) {

    /**
     * 해당 메소드를 사용할 때 noteGroupIds가 null이나 empty가 아닌지 검증하고 사용해야한다.
     */
    fun getRecommendNoteGroupId(perfumeIds: Set<Long>, noteGroupIds: List<Long>): NoteGroupDetailVo {
        if (noteGroupIds.isNullOrEmpty())
            throw IllegalArgumentException("서버에서 잘못된 요청을 허용했습니다. noteGroupIds: $noteGroupIds")
        if (perfumeIds.isNullOrEmpty()) return noteGroupService.getDetailById(noteGroupIds.shuffled()[0])

        var perfume = perfumeRepository.findPerfumeById(perfumeIds.elementAt(0))
            ?: throw PerfumeNotFoundException("Perfume not found. id: ${perfumeIds.elementAt(0)}")
        var perfumeNoteGroupIds = perfume.notes
            .mapNotNull { it.note.noteGroup?.id }
            .toSet()
        if (perfumeIds.size > 1)  {
            var unionNoteGroupIds = perfumeNoteGroupIds
            for (index in 1 until perfumeIds.size) {
                unionNoteGroupIds = unionNoteGroupIds.filter {
                    perfume = perfumeRepository.findPerfumeById(perfumeIds.elementAt(index))
                        ?: throw PerfumeNotFoundException("Perfume not found. perfumeId: ${perfumeIds.elementAt(index)}")
                    perfume.notes
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
    }
}