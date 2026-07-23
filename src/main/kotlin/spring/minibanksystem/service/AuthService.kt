package spring.minibanksystem.service

import spring.minibanksystem.dto.ResponseDto
import spring.minibanksystem.dto.request.AuthRequest
import spring.minibanksystem.dto.response.AuthResponse

interface AuthService {
    fun register(request : AuthRequest) : ResponseDto<AuthResponse>
}