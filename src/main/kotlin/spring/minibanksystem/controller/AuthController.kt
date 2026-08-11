package spring.minibanksystem.controller

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
class AuthController(
    private val authService: AuthService,
) {

    @PostMapping("/register")
    fun register(@Valid @RequestBody request : RegisterRequest): ResponseEntity<ResponseSuccess<RegisterResponse>> {
        val response = authService.register(request)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<ResponseSuccess<LoginResponse>> {
        val response = authService.login(request)
        return ResponseEntity.ok(response)
    }
}
