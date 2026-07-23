package spring.minibanksystem.dto

import org.springframework.http.HttpStatus

data class ResponseDto<T> (
    val success : Boolean,
    val status : HttpStatus,
    val data : T?,
    val message : String?
)