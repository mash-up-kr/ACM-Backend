package mashup.backend.spring.acm.presentation.api.search

import mashup.backend.spring.acm.application.search.SearchApplicationService
import mashup.backend.spring.acm.presentation.ApiResponse
import mashup.backend.spring.acm.presentation.assembler.toDto
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/search")
class SearchController(
    private val searchApplicationService: SearchApplicationService,
) {
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
