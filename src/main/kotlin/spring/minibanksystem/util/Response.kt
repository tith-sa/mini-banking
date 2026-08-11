package spring.minibanksystem.util

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spring.minibanksystem.dto.ResponseError
import spring.minibanksystem.dto.ResponseSuccess

fun <T> T.toSuccess(
    status: HttpStatus = HttpStatus.OK,
    message: String? = null
): ResponseSuccess<T> {
    return ResponseSuccess(
        status,
        this,
        message
    )
}

fun HttpStatus.buildError(
    message: String?,
): ResponseEntity<ResponseError> {
    val body = ResponseError(
        this,
        message = message
    )
    return ResponseEntity
        .status(this)
        .body(body)
}