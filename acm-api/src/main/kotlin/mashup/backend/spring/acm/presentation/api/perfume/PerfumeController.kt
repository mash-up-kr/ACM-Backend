package mashup.backend.spring.acm.presentation.api.perfume

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import mashup.backend.spring.acm.application.perfume.PerfumeApplicationService
import mashup.backend.spring.acm.application.recommend.RecommendApplicationService
import mashup.backend.spring.acm.domain.perfume.PerfumeService
import mashup.backend.spring.acm.presentation.ApiResponse
import mashup.backend.spring.acm.presentation.assembler.toDto
import mashup.backend.spring.acm.presentation.assembler.toPerfumeDetail
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*

@Api(
    description = "향수",
    tags = ["perfume"],
)
@RequestMapping("/api/v1/perfumes")
@RestController
class PerfumeController(
    private val perfumeService: PerfumeService,
    private val recommendApplicationService: RecommendApplicationService,
    private val perfumeApplicationService: PerfumeApplicationService,
) {
    @ApiOperation(value = "향수 목록 조회")
    @GetMapping
    fun getPerfumes(
        brandId: Long?,
        noteId: Long?,
        pageable: Pageable,
    ): ApiResponse<PerfumeListData> {
        return ApiResponse.success(
            data = PerfumeListData(
                perfumes = perfumeApplicationService.getPerfumes(
                    brandId = brandId,
                    noteId = noteId,
                    pageable = pageable,
                ).map { it.toDto() }
            )
        )
    }

    @ApiOperation(value = "향수 상세보기 API")
    @GetMapping("/{perfumeId}")
    fun getPerfumeDetail(@PathVariable perfumeId: Long): ApiResponse<PerfumeDetailResponse> {
        val perfume = perfumeService.getPerfume(perfumeId)
        val similarPerfumes = recommendApplicationService.recommendSimilarPerfumes(perfumeId)

        return ApiResponse.success(PerfumeDetailResponse(perfume.toPerfumeDetail(similarPerfumes)))
    }
}
