package mashup.backend.spring.acm.presentation.assembler

import mashup.backend.spring.acm.application.search.SearchRequestVo
import mashup.backend.spring.acm.application.search.SearchResponseVo
import mashup.backend.spring.acm.presentation.api.search.SearchData
import mashup.backend.spring.acm.presentation.api.search.SearchRequest
import org.springframework.data.domain.PageRequest

fun SearchRequest.toVo(): SearchRequestVo = SearchRequestVo(
    name = this.name!!,
    type = this.type,
    pageable = PageRequest.of(this.page, this.size),
)

fun SearchResponseVo.toDto(): SearchData = SearchData(
    brands = this.brands.map { it.toDto() },
    perfumes = this.perfumes.map { it.toDto() },
)
