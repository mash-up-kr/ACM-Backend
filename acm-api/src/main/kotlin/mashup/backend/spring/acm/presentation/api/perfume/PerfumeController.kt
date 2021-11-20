package mashup.backend.spring.acm.presentation.api.perfume

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import mashup.backend.spring.acm.application.recommend.RecommendApplicationService
import mashup.backend.spring.acm.domain.perfume.PerfumeService
import mashup.backend.spring.acm.presentation.ApiResponse
import mashup.backend.spring.acm.presentation.assembler.toPerfumeDetail
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore

@Api(
    description = "향수",
    tags = ["perfume"],
)
@RequestMapping("/api/v1/perfumes")
@RestController
class PerfumeController(
    val perfumeService: PerfumeService,
    val recommendApplicationService: RecommendApplicationService
) {
    @ApiOperation(value = "향수 상세보기 API")
    @GetMapping("/{id}")
    fun getPerfumeDetail(@PathVariable id: Long): ApiResponse<PerfumeDetailResponse> {
        val perfume = perfumeService.getPerfume(id)
        val similarPerfumes = recommendApplicationService.recommendSimilarlyPerfumes(id)
        println(similarPerfumes)

        return ApiResponse.success(PerfumeDetailResponse(perfume.toPerfumeDetail(similarPerfumes)))
    }
}
