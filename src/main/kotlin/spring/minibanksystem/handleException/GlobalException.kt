package spring.minibanksystem.handleException

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import spring.minibanksystem.dto.ResponseDto
import spring.minibanksystem.util.buildError

@RestControllerAdvice
class GlobalException {
    @ExceptionHandler(HandleException.BadRequest::class)
    fun handleBadRequest(ex: HandleException.BadRequest): ResponseEntity<ResponseDto<Nothing>> {
        return HttpStatus.BAD_REQUEST.buildError(ex.message)
    }

    @ExceptionHandler(HandleException.ResourceNotFound::class)
    fun handleResourceNotFound(ex: HandleException.ResourceNotFound): ResponseEntity<ResponseDto<Nothing>> {
        return HttpStatus.NOT_FOUND.buildError(ex.message)
    }

    @ExceptionHandler(HandleException.Authorization::class)
    fun handleAuthorizationError(ex: HandleException.Authorization): ResponseEntity<ResponseDto<Nothing>> {
        return HttpStatus.FORBIDDEN.buildError(ex.message)
    }

    @ExceptionHandler(HandleException.Authentication::class)
    fun handleAuthenticationError(ex: HandleException.Authentication): ResponseEntity<ResponseDto<Nothing>> {
        return HttpStatus.UNAUTHORIZED.buildError(ex.message)
    }

    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(ex: RuntimeException): ResponseEntity<ResponseDto<Nothing>> {
        return HttpStatus.INTERNAL_SERVER_ERROR.buildError(ex.message)
    }
}