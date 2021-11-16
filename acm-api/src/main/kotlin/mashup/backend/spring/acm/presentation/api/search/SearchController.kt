package mashup.backend.spring.acm.presentation.api.search

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import mashup.backend.spring.acm.application.search.SearchApplicationService
import mashup.backend.spring.acm.presentation.ApiResponse
import mashup.backend.spring.acm.presentation.assembler.toDto
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@Api(
    description = "검색",
    tags = ["search"],
)
@RestController
@RequestMapping("/api/v1/search")
class SearchController(
    private val searchApplicationService: SearchApplicationService,
) {
    @ApiOperation(
        value = "이름으로 향수, 브랜드 통합 검색",
        notes = "brands, perfumes 각각 30개만 결과에 포함됨",
    )
    @PostMapping
    fun search(
        @RequestBody @Valid searchRequest: SearchRequest,
    ): ApiResponse<SearchData> {
        val (brands, perfumes) = searchApplicationService.search(name = searchRequest.name!!)
        return ApiResponse.success(
            data = SearchData(
                brands = brands.map { it.toDto() },
                perfumes = perfumes.map { it.toDto() }
            )
        )
    }
}
