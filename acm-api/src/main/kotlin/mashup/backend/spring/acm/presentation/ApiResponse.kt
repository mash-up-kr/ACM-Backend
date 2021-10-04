package mashup.backend.spring.acm.presentation

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

data class ApiResponse<T>(
    val code: String,
    val message: String,
    val data: T? = null
) {
    companion object {
        fun <T> empty(): ApiResponse<T> {
            return ApiResponse("", "", null)
        }

        fun <T> success(data: T): ApiResponse<T> {
            return ApiResponse("", "", data)
        }

        fun failure(code: String, message: String): ApiResponse<*> {
            return ApiResponse<Any>(code, message, null)
        }
    }
}

/**
 * example
 * ------------------------------
 * fun tmp2(): ApiResponse<PageData<TmpDto>> {
 *  return ApiResponse.success(tmp())
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