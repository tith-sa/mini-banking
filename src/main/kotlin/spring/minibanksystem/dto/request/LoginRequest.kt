package spring.minibanksystem.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class LoginRequest(

    @Email(message = "Must be a valid email address")
    val email: String?,

    @field:NotBlank
    val password: String?
)
