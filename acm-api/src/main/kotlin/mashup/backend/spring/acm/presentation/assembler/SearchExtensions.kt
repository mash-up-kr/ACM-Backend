package mashup.backend.spring.acm.presentation.assembler

import mashup.backend.spring.acm.application.search.SearchRequestVo
import mashup.backend.spring.acm.application.search.SearchResponseVo
import mashup.backend.spring.acm.presentation.api.search.SearchRequest
import mashup.backend.spring.acm.presentation.api.search.SearchResponse
import mashup.backend.spring.acm.presentation.api.search.SearchType
import org.springframework.data.domain.PageRequest

fun SearchRequest.toVo(): SearchRequestVo = SearchRequestVo(
    name = this.name!!,
    type = this.type?.let { SearchType.valueOf(it) } ?: SearchType.ALL,
    pageable = PageRequest.of(this.page!!, this.size!!),
)

fun SearchResponseVo.toSearchResponse(): SearchResponse = SearchResponse(
    brands = this.brands.map { it.toDto() },
    perfumes = this.perfumes.map { it.toDto() },
)
