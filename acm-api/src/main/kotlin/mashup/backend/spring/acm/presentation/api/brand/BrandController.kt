package mashup.backend.spring.acm.presentation.api.brand

import mashup.backend.spring.acm.presentation.ApiResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/brands")
class BrandController {
    @PostMapping("/search")
    fun search(
        @RequestBody brandSearchRequest: BrandSearchRequest,
    ): ApiResponse<BrandSearchResponse> {
        TODO()
    }
}