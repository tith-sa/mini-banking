package spring.minibanksystem.controller

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import spring.minibanksystem.dto.ResponseDto
import spring.minibanksystem.dto.request.AuthRequest
import spring.minibanksystem.dto.response.AuthResponse
import spring.minibanksystem.service.AuthService

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService,
) {

    @PostMapping("/register")
    fun register(@Valid @RequestBody request : AuthRequest): ResponseEntity<ResponseDto<AuthResponse>> {
        val response = authService.register(request)
        return ResponseEntity.ok(response)
    }
}
