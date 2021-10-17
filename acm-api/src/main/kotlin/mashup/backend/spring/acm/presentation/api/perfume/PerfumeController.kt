package mashup.backend.spring.acm.presentation.api.perfume

import io.swagger.annotations.ApiOperation
import mashup.backend.spring.acm.domain.perfume.PerfumeService
import mashup.backend.spring.acm.presentation.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/v1/perfumes")
@RestController
class PerfumeController(val perfumeService: PerfumeService) {
    @ApiOperation(value = "[v1.0.0] 향수 상세보기 API")
    @GetMapping("/{id}")
    fun getPerfumeDetail(@PathVariable id: Long): ApiResponse<PerfumeDetailResponse> {
        val perfumes = perfumeService.getPerfume(id)
        val similarPerfumes = SimpleSimilarPerfume.of(perfumeService.getSimilarPerfume(id))

        return ApiResponse.success(PerfumeDetailResponse(PerfumeDetail.of(perfumes, similarPerfumes)))
    }
}