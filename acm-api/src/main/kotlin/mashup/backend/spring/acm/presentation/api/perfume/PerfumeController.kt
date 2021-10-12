package mashup.backend.spring.acm.presentation.api.perfume

import mashup.backend.spring.acm.domain.perfume.PerfumeService
import mashup.backend.spring.acm.presentation.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/perfume")
@RestController
class PerfumeController(val perfumeService: PerfumeService) {
    @GetMapping("/{id}")
    fun getPerfumeDetail(@PathVariable id: Long): ApiResponse<PerfumeDetailResponse> {
        val perfumes = perfumeService.getPerfume(id)
        val similarPerfumes = SimpleSimilarPerfume.of(perfumeService.getSimilarPerfume(id))

        return ApiResponse.success(PerfumeDetailResponse(PerfumeDetail.of(perfumes, similarPerfumes)))
    }
}