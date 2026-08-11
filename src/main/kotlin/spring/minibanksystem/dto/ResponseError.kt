package spring.minibanksystem.dto

import org.springframework.http.HttpStatus
import spring.minibanksystem.config.AppConstants
import java.time.format.DateTimeFormatter
import java.time.Instant
import java.time.ZoneId

data class ResponseError(
    val status: HttpStatus,
    val data: Any? = null,
    val message: String?,
    val errors: List<Error>? = emptyList(),
    val timestamp: String = DateTimeFormatter
        .ofPattern(AppConstants.DATETIME_PATTERN)
        .withZone(ZoneId.of(AppConstants.LOCAL_TZ))
        .format(Instant.now())

){
    data class Error(
        val field: String,
        val message: String?,
    )
}
