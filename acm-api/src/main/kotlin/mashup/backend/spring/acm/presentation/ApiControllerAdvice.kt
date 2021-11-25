package mashup.backend.spring.acm.presentation

import mashup.backend.spring.acm.domain.ResultCode
import mashup.backend.spring.acm.domain.exception.BadRequestException
import mashup.backend.spring.acm.domain.exception.BusinessException
import mashup.backend.spring.acm.domain.exception.NotFoundException
import mashup.backend.spring.acm.infrastructure.spring.mvc.EnumConversionFailedException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.convert.ConversionFailedException
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.MethodArgumentNotValidException
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
     * 요청에 오류가 있는 경우 (invalid parameter)
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ApiResponse<Unit> {
        log.error("MethodArgumentNotValidException", e)
        return ApiResponse.failure(
            code = ResultCode.BAD_REQUEST.name,
            message = e.fieldErrors.joinToString {
                "${it.field}: ${it.defaultMessage}"
            },
        )
    }

    /**
     * 요청에 오류가 있는 경우 (Http Message 파싱 실패)
     */
    @ExceptionHandler(HttpMessageNotReadableException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleHttpMessageNotReadableException(e: HttpMessageNotReadableException): ApiResponse<Unit> {
        log.error("HttpMessageNotReadableException", e)
        return ApiResponse.failure(
            code = ResultCode.BAD_REQUEST.name,
            message = e.mostSpecificCause.message ?: ResultCode.BAD_REQUEST.message
        )
    }

    /**
     * 요청에 오류가 있는 경우
     */
    @ExceptionHandler(BadRequestException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleBadRequestException(e: BadRequestException): ApiResponse<Unit> {
        log.error("BadRequestException", e)
        return ApiResponse.failure(e.resultCode)
    }

    /**
     * 리소스 조회 실패
     */
    @ExceptionHandler(NotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNotFoundException(e: NotFoundException): ApiResponse<Unit> {
        log.error("NotFoundException", e)
        return ApiResponse.failure(e.resultCode)
    }

    /**
     * 내부 로직에 의해 발생하는 예외
     */
    @ExceptionHandler(BusinessException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleBusinessException(e: BusinessException): ApiResponse<Unit> {
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