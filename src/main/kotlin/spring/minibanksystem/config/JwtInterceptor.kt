package spring.minibanksystem.config

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import spring.minibanksystem.handleException.HandleException

@Component
class JwtInterceptor(
    private val jwtUtil: JwtUtil
): HandlerInterceptor {
    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any
    ): Boolean {
        val authHeader = request.getHeader("Authorization")
        if (authHeader.isNullOrBlank() || !authHeader.startsWith("Bearer ")) {
            throw HandleException.Authentication("Invalid Authorization")
        }
        val token = authHeader.removePrefix("Bearer ")

       try {
           val userId = jwtUtil.getUserId(token)
            request.setAttribute("userId", userId)
           return true
       }catch (ex: Exception) {
           throw HandleException.Authentication("Invalid Authorization")
       }
    }
}