package spring.minibanksystem.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import spring.minibanksystem.dto.ResponseSuccess
import spring.minibanksystem.dto.request.LoginRequest
import spring.minibanksystem.dto.request.RegisterRequest
import spring.minibanksystem.dto.response.LoginResponse
import spring.minibanksystem.dto.response.RegisterResponse
import spring.minibanksystem.service.interfaceService.AuthService

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth API")
class AuthController(
    private val authService: AuthService,
) {

    @PostMapping("/register")
    @Operation(summary = "Register", description = "Create a new user")
    fun register(@Valid @RequestBody request : RegisterRequest): ResponseEntity<ResponseSuccess<RegisterResponse>> {
        val response = authService.register(request)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Login User")
    fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<ResponseSuccess<LoginResponse>> {
        val response = authService.login(request)
        return ResponseEntity.ok(response)
    }
}
