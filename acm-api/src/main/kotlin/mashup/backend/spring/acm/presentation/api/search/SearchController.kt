package mashup.backend.spring.acm.presentation.api.search

import mashup.backend.spring.acm.presentation.ApiResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/search")
class SearchController {
    @PostMapping
    fun search(
        @RequestBody searchRequest: SearchRequest,
    ): ApiResponse<SearchData> = TODO()
}
