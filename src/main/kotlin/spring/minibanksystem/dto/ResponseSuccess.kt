package spring.minibanksystem.dto

import org.springframework.http.HttpStatus

data class ResponseSuccess<T> (
    val status : HttpStatus,
    val data : T?,
    val message : String?
)