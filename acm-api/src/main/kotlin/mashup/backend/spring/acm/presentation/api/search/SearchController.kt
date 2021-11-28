package mashup.backend.spring.acm.presentation.api.search

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import mashup.backend.spring.acm.application.search.SearchApplicationService
import mashup.backend.spring.acm.presentation.ApiResponse
import mashup.backend.spring.acm.presentation.assembler.toSearchResponse
import mashup.backend.spring.acm.presentation.assembler.toVo
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
        value = "[v1] 향수 & 브랜드 이름 검색 API",
        notes = "이름으로 검색 시 결과\n"
                + "전체(ALL): 브랜드 최대 1개 + 향수 최대 30개씩\n"
                + "브랜드(BRAND): 검색되는 브랜드 최대 30개씩\n"
                + "향수(PERFUME): 검색되는 향수 최대 30개씩\n"
    )
    @PostMapping
    fun search(
        @RequestBody @Valid searchRequest: SearchRequest,
    ): ApiResponse<SearchResponse> {
        return ApiResponse.success(
            data = searchApplicationService.search(searchRequestVo = searchRequest.toVo()).toSearchResponse()
        )
    }
}
