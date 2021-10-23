package mashup.backend.spring.acm.presentation

import com.fasterxml.jackson.annotation.JsonInclude
import mashup.backend.spring.acm.domain.ResultCode
import org.springframework.data.domain.Page

data class ApiResponse<T>(
    val code: String,
    val message: String,
    val data: T? = null
) {
    companion object {
        fun <T> empty(): ApiResponse<T> {
            return ApiResponse("", "", null)
        }

        fun <T> success() = ApiResponse<T>(ResultCode.SUCCESS.name, ResultCode.SUCCESS.message, null)

        fun <T> success(data: T) = ApiResponse(ResultCode.SUCCESS.name, ResultCode.SUCCESS.message, data)

        fun failure(code: String, message: String) = ApiResponse<Unit>(code, message, null)

        fun failure(resultCode: ResultCode) = ApiResponse<Unit>(resultCode.name, resultCode.message, null)
    }
}

/**
 * example
 * ------------------------------
 * fun tmp2(): ApiResponse<PageData<TmpDto>> {
 *  return ApiResponse.success(PageResponse(tmo()))
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