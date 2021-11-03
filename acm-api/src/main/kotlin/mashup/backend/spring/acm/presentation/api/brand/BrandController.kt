package mashup.backend.spring.acm.presentation.api.brand

import mashup.backend.spring.acm.application.brand.BrandApplicationService
import mashup.backend.spring.acm.presentation.ApiResponse
import mashup.backend.spring.acm.presentation.assembler.toDto
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/brands")
class BrandController(
    private val brandApplicationService: BrandApplicationService,
) {
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