package spring.minibanksystem.service.interfaceService

import spring.minibanksystem.dto.ResponseSuccess
import spring.minibanksystem.dto.request.LoginRequest
import spring.minibanksystem.dto.request.RegisterRequest
import spring.minibanksystem.dto.response.RegisterResponse
import spring.minibanksystem.dto.response.LoginResponse

interface AuthService {
    fun register(request : RegisterRequest) : ResponseSuccess<RegisterResponse>
    fun login(request: LoginRequest) : ResponseSuccess<LoginResponse>
}