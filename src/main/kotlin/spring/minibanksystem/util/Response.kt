package spring.minibanksystem.util

import org.springframework.http.HttpStatus
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