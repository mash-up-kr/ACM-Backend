package mashup.backend.spring.acm.application.perfume

import mashup.backend.spring.acm.application.ApplicationService
import mashup.backend.spring.acm.domain.perfume.PerfumeService
import mashup.backend.spring.acm.domain.perfume.PerfumeSimpleVo
import org.springframework.data.domain.Pageable

@ApplicationService
class PerfumeApplicationService(
    private val perfumeService: PerfumeService,
) {
    fun getPerfumes(
        brandId: Long?,
        noteId: Long?,
        pageable: Pageable,
    ): List<PerfumeSimpleVo> {
        return if (brandId == null && noteId == null) {
            perfumeService.getPerfumes(pageable).content
        }
        else if (brandId != null && noteId == null) {
            perfumeService.getPerfumesByBrandId(
                brandId = brandId,
                pageable = pageable
            ).content
        }
        else if (noteId != null && brandId == null) {
            perfumeService.getPerfumesByNoteId(
                noteId = noteId,
                pageable = pageable
            ).content
        } else {
            perfumeService.getPerfumes(
                brandId = brandId!!,
                noteId = noteId!!,
                pageable = pageable
            ).content
        }
    }
}