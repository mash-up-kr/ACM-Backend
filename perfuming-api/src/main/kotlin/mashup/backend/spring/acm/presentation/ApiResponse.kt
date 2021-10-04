package mashup.backend.spring.acm.presentation

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.data.domain.Page

data class ApiResponse<T>(
    val code: String,
    val message: String,
    val data: T? = null,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val totalElements: Long? = null,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val hasNext: Boolean? = null
) {
    companion object {
        fun <T> empty(): ApiResponse<T> {
            return ApiResponse("", "", null, null, null)
        }

        fun <T> success(data: T): ApiResponse<T> {
            return ApiResponse("", "", data, null, null)
        }

        fun <T> success(data: Page<T>): ApiResponse<List<T>> {
            return ApiResponse("", "", data.content, data.totalElements, data.hasNext())
        }

        fun <T> success(data: List<T>): ApiResponse<List<T>> {
            return ApiResponse("", "", data, data.size.toLong(), false)
        }

        fun failure(code: String, message: String): ApiResponse<*> {
            return ApiResponse<Any>(code, message, null, null, null)
        }
    }
}