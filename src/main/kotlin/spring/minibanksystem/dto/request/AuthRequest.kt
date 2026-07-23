package spring.minibanksystem.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class AuthRequest (
    @field:NotBlank(message = "Name is required")
    val username : String,

    @Email(message = "Email is required")
    val email : String,

    @field:NotBlank(message = "Password is required")
    val password : String,
)