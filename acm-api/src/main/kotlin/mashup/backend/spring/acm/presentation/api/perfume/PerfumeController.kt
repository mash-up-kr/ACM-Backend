package mashup.backend.spring.acm.presentation.api.perfume

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import mashup.backend.spring.acm.domain.perfume.PerfumeService
import mashup.backend.spring.acm.presentation.ApiResponse
import mashup.backend.spring.acm.presentation.assembler.toPerfumeDetail
import org.springframework.web.bind.annotation.*

@Api(
    description = "향수",
    tags = ["perfume"],
)
@RequestMapping("/api/v1/perfumes")
@RestController
class PerfumeController(val perfumeService: PerfumeService) {
    @ApiOperation(value = "향수 상세보기 API")
    @GetMapping("/{id}")
    fun getPerfumeDetail(@PathVariable id: Long): ApiResponse<PerfumeDetailResponse> {
        val perfume = perfumeService.getPerfume(id)
        val similarPerfumes = SimpleSimilarPerfume.of(perfumeService.getSimilarPerfume(id))

        return ApiResponse.success(PerfumeDetailResponse(perfume.toPerfumeDetail(similarPerfumes)))
    }
}
