package spring.minibanksystem.handleException

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import spring.minibanksystem.dto.ResponseError
import spring.minibanksystem.util.buildError

@RestControllerAdvice
class GlobalException {

    // 1. Validation Error Handling (Maps specific invalid fields to their error messages)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(ex: MethodArgumentNotValidException): ResponseEntity<ResponseError> {

        val errors = ex.bindingResult.fieldErrors.map {
            ResponseError.Error(
                field = it.field,
                message = it.defaultMessage
            )
        }
        val response = ResponseError(
            status = HttpStatus.BAD_REQUEST,
            message = "Validation failed",
            errors = errors
        )

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response)
    }

    @ExceptionHandler(HandleException.BadRequest::class)
    fun handleBadRequest(ex: HandleException.BadRequest): ResponseEntity<ResponseError> {
        return HttpStatus.BAD_REQUEST.buildError(ex.message)
    }

    @ExceptionHandler(HandleException.ResourceNotFound::class)
    fun handleResourceNotFound(ex: HandleException.ResourceNotFound): ResponseEntity<ResponseError> {
        return HttpStatus.NOT_FOUND.buildError(ex.message)
    }

    @ExceptionHandler(HandleException.Authorization::class)
    fun handleAuthorizationError(ex: HandleException.Authorization): ResponseEntity<ResponseError> {
        return HttpStatus.FORBIDDEN.buildError(ex.message)
    }

    @ExceptionHandler(HandleException.Authentication::class)
    fun handleAuthenticationError(ex: HandleException.Authentication): ResponseEntity<ResponseError> {
        return HttpStatus.UNAUTHORIZED.buildError(ex.message)
    }

    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(ex: RuntimeException): ResponseEntity<ResponseError> {
        return HttpStatus.INTERNAL_SERVER_ERROR.buildError(ex.message)
    }
}