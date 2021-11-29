package mashup.backend.spring.acm.application.search

import mashup.backend.spring.acm.presentation.api.search.SearchType
import org.springframework.data.domain.Pageable

data class SearchRequestVo(
    val name: String,
    val type: SearchType,
    val pageable: Pageable,
)