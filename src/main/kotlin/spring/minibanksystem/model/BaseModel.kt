package spring.minibanksystem.model

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import spring.minibanksystem.config.AppConstants
import java.time.LocalDateTime

//sharing the same field
@MappedSuperclass
abstract class BaseModel(

    @CreationTimestamp
    @JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = AppConstants.DATETIME_PATTERN,
        timezone = AppConstants.ZONE_ID
    )
    @Column("createdAt", nullable = false, updatable = false)
    var createdAt: LocalDateTime? = null,

    @UpdateTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING,
        pattern = AppConstants.DATETIME_PATTERN,
        timezone = AppConstants.ZONE_ID
    )
    @Column("updatedAt", nullable = false)
    var updatedAt: LocalDateTime? = null,
)
