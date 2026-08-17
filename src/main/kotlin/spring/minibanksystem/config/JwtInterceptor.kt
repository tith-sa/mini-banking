package spring.minibanksystem.config

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import spring.minibanksystem.handleException.AuthenticationException

@Component
class JwtInterceptor(
    private val jwtUtil: JwtUtil
): HandlerInterceptor {

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any
    ): Boolean {

        // Get the Authorization header from the incoming request.
        // Expected format: "Bearer <JWT_TOKEN>"
        val authHeader = request.getHeader("Authorization")

        // Check whether the Authorization header is missing, blank,
        // or does not start with the required "Bearer " prefix.
        // If invalid, reject the request with an authentication error.
        if (authHeader.isNullOrBlank() || !authHeader.startsWith("Bearer ")) {
            throw AuthenticationException("Invalid Authorization")
        }

        // Remove the "Bearer " prefix and keep only the JWT token.
        val token = authHeader.removePrefix("Bearer ")

       try {

           // Extract the user ID from the JWT token.
           // JwtUtil is responsible for validating and decoding the token.
           val userId = jwtUtil.getUserId(token)

           // Store the authenticated user's ID in the request.
           // Controllers/services can retrieve it using:
           // request.getAttribute("userId")
            request.setAttribute("userId", userId)

           // Return true to allow the request to continue to the controller.
           return true
       }catch (ex: Exception) {

           // If the token is invalid, expired, malformed,
           // or cannot be decoded, reject the request.
           throw AuthenticationException("Invalid Authorization")
       }
    }
}