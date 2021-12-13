package mashup.backend.spring.acm.domain.recommend.notegroup

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
     * 주어진 정보로부터 노트그룹 1개 추천
     * TODO: 랜덤보다는 노트 그룹에 속한 노트 개수가 많은 순서 등을 이용해서 가산점을 주는 방식 등으로 개선
     *
     * @param perfumeIds 이미 선택된 향수 id 목록
     * @param noteGroupIds 향수 추천에 사용할 노트 그룹 id 후보군
     */
    fun getRecommendNoteGroupId(perfumeIds: Set<Long>, noteGroupIds: List<Long>): NoteGroupDetailVo {
        if (noteGroupIds.isEmpty()) {
            throw IllegalArgumentException("'noteGroup' must not be empty. noteGroupIds: $noteGroupIds")
        }
        // 향수 id 가 주어지지 않으면 임의의 노트 그룹 리턴
        if (perfumeIds.isNullOrEmpty()) {
            return noteGroupService.getDetailById(noteGroupIds.shuffled()[0])
        }

        // TODO: querydsl 추가하면 join 하는 쿼리로 개선해야함
        // 향수들을 이루는 모든 노트의 노트 그룹을 수집
        val perfumesNoteGroupIds = perfumeIds.mapNotNull { perfumeRepository.findPerfumeById(it) }
            .flatMap { it.notes }
            .mapNotNull { it.note.noteGroup }
            .map { it.id }
            .toSet()

        // parameter 로 입력받은 noteGroupId 보다, parameter 에 없는데 향수로부터 추출한 noteGroupId 를 우선으로 사용한다.
        val newNoteGroupIds = perfumesNoteGroupIds - noteGroupIds
        if (newNoteGroupIds.isNotEmpty()) {
            return noteGroupService.getDetailById(newNoteGroupIds.first())
        }
        return noteGroupService.getDetailById(noteGroupIds.first())
    }
}