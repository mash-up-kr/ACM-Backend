package mashup.backend.spring.acm.presentation.api.brand

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import mashup.backend.spring.acm.application.brand.BrandApplicationService
import mashup.backend.spring.acm.presentation.ApiResponse
import mashup.backend.spring.acm.presentation.assembler.toDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Api(
    description = "브랜드",
    tags = ["brand"],
)
@RestController
@RequestMapping("/api/v1/brands")
class BrandController(
    private val brandApplicationService: BrandApplicationService,
) {
    @ApiOperation(
        value = "브랜드 상세 조회",
    )
    @GetMapping("/{brandId}")
    fun getBrand(
        @PathVariable brandId: Long,
    ): ApiResponse<BrandDetailData> {
        return ApiResponse.success(
            data = BrandDetailData(
                brand = brandApplicationService.getBrand(brandId = brandId).toDto(),
            ),
        )
    }
}