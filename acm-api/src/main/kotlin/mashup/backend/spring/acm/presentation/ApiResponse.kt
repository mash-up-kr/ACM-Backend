package mashup.backend.spring.acm.presentation

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

data class ApiResponse(
    val code: String,
    val message: String,
    val data: Map<String, Any> = emptyMap()
) {
    companion object {
        fun empty(): ApiResponse {
            return ApiResponse("", "")
        }

        fun success(name: String, data: Any): ApiResponse {
            return ApiResponse("", "", mapOf(name to data))
        }

        fun failure(code: String, message: String): ApiResponse {
            return ApiResponse(code, message)
        }
    }
}

/**
 * example
 * ------------------------------
 * fun tmp2(): ApiResponse<PageData<TmpDto>> {
 *  return ApiResponse.success("tmps", tmp())
 * }
 *
 * fun tmp(): PageData<TmpDto> {
 *  return PageData.of(Page.empty())
 * }
 */
data class PageData<T>(
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val totalElements: Long? = null,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val hasNext: Boolean? = null,
    val elements: List<T> = emptyList()
) {
    companion object {
        fun <T> of(data: Page<T>): PageData<T> {
            return PageData(data.totalElements, data.hasNext(), data.content)
        }

        fun <T> empty(): PageData<T> {
            return PageData(0L, false, emptyList())
        }
    }
}