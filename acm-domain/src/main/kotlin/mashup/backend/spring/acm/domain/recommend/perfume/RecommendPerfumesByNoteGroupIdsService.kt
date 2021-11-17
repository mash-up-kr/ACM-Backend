package mashup.backend.spring.acm.domain.recommend.perfume

import mashup.backend.spring.acm.domain.note.NoteGroupService
import mashup.backend.spring.acm.domain.perfume.Perfume
import mashup.backend.spring.acm.domain.perfume.PerfumeService
import mashup.backend.spring.acm.domain.recommend.RecommendRequestVo
import org.springframework.stereotype.Service

@Service
class RecommendPerfumesByNoteGroupIdsAndGenderAndAgeService(
    private val noteGroupService: NoteGroupService,
    private val perfumeService: PerfumeService
): RecommendPerfumesService {
    override fun supports(recommendRequestVo: RecommendRequestVo): Boolean {
        val member = recommendRequestVo.memberDetailVo!!
        return member.hasNoteGroupIds() && member.hasGender()
    }

    override fun getItems(recommendRequestVo: RecommendRequestVo): List<Perfume> {
        val notes = noteGroupService.getRecommendNoteGroupId(
            recommendRequestVo.exceptIds ?: emptySet(),
            recommendRequestVo.memberDetailVo!!.noteGroupIds
        ).notes.shuffled()
        val size = recommendRequestVo.size
        val perfumes = mutableListOf<Perfume>()
        val member = recommendRequestVo.memberDetailVo

        if (notes.size > size) {
            notes.subList(0, size - perfumes.size).forEach { note ->
                perfumeService.getPerfumesByNoteIdAndGender(note.id, member.getPerfumeGender(), 1).forEach { perfume ->
                    perfumes.add(perfume)
                }
            }

            return perfumes
        }

        // 노트그룹의 노트 개수가 count 미만이면, 하나의 노트에서 count 만큼 향수 추천
        perfumeService.getPerfumesByNoteIdAndGender(notes[0].id, member.getPerfumeGender(), size - perfumes.size).forEach {
            perfumes.add(it)
        }

        return emptyList()
    }


}