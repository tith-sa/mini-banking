package spring.minibanksystem.util

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spring.minibanksystem.dto.ResponseDto

fun <T> T.toSuccess(
    status: HttpStatus = HttpStatus.OK,
    message: String? = null
): ResponseDto<T> {
    return ResponseDto(
        true,
        status,
        this,
        message
    )
}

fun HttpStatus.buildError(message: String?): ResponseEntity<ResponseDto<Nothing>> {
    val body = ResponseDto(
        false,
        this,
        null,
        message ?: "An error occurred",
    )
    return ResponseEntity.status(this).body(body)
}