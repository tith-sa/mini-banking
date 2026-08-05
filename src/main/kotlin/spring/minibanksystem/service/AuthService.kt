package spring.minibanksystem.service

import spring.minibanksystem.dto.ResponseDto
import spring.minibanksystem.dto.request.LoginRequest
import spring.minibanksystem.dto.request.RegisterRequest
import spring.minibanksystem.dto.response.RegisterResponse
import spring.minibanksystem.dto.response.LoginResponse

interface AuthService {
    fun register(request : RegisterRequest) : ResponseDto<RegisterResponse>
    fun login(request: LoginRequest) : ResponseDto<LoginResponse>
}