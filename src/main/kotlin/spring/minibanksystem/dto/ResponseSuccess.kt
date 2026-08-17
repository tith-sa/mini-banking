package spring.minibanksystem.dto

import org.springframework.http.HttpStatus
import spring.minibanksystem.config.AppConstants
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class ResponseSuccess<T> (
    val status : HttpStatus = HttpStatus.OK,
    val data : T?,
    val message : String?,
    val timestamp : String = DateTimeFormatter
        .ofPattern(AppConstants.DATETIME_PATTERN)
        .withZone(ZoneId.of(AppConstants.LOCAL_TZ))
        .format(Instant.now())
)