package spring.minibanksystem.util

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spring.minibanksystem.dto.ResponseError

fun HttpStatus.buildError(
    message: String?,
    data: Any? = null
): ResponseEntity<ResponseError> {
    val body = ResponseError(
        status = this,
        data = data,
        message = message
    )
    return ResponseEntity
        .status(this)
        .body(body)
}