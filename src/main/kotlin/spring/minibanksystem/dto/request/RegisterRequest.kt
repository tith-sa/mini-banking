package spring.minibanksystem.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.intellij.lang.annotations.Pattern

data class RegisterRequest (
    @field:NotBlank(message = "Name is required")
    val username : String,

    @Email(message = "Email is required")
    val email : String,

    @field:Size(min = 6, message = "Password must be at least 6 characters")
    val password : String,
)