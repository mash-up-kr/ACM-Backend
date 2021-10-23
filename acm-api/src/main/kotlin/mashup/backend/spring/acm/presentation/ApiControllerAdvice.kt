package mashup.backend.spring.acm.presentation

import mashup.backend.spring.acm.BusinessException
import mashup.backend.spring.acm.domain.ResultCode
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.security.Principal

@RestControllerAdvice
class ApiControllerAdvice {
    @ModelAttribute("memberId")
    fun resolveMemberId(principal: Principal?): Long? {
        log.debug("principal : $principal")
        if (principal is UsernamePasswordAuthenticationToken) {
            return principal.principal.toString().toLong()
        }
        return null
    }

    /**
     * 4xx, 5xx 으로 상태코드 구분해서 응답해야하나 일단 500 으로 응답함
     * - 4xx : 클라이언트 문제, 재시도해도 실패함
     * - 5xx : 서버 문제, 재시도하면 성공할수도 있음
     */
    @ExceptionHandler(BusinessException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleException(e: BusinessException): ApiResponse<Unit> {
        log.error("BusinessException", e)
        return ApiResponse.failure(e.resultCode)
    }

    /**
     * 처리하지 않은 예외
     */
    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleException(e: Exception): ApiResponse<Unit> {
        log.error("Exception", e)
        return ApiResponse.failure(ResultCode.INTERNAL_SERVER_ERROR)
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(ApiControllerAdvice::class.java)
    }
}