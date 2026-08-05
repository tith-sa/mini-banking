package spring.minibanksystem.config

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.util.Date

@Component
class JwtUtil {
    private val secret = Keys.hmacShaKeyFor(
            "abcdefghijklmnopqrstuvwxyz123456".toByteArray()
        )

    fun generateToken(userId: Long, email: String): String{
        return Jwts.builder()
            .subject(email)
            .claim("userId", userId)
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + 86400000))
            .signWith(secret)
            .compact()
    }

    private fun getClaims(token: String) =
        Jwts.parser()
            .verifyWith(secret)
            .build()
            .parseSignedClaims(token)
            .payload

    fun getUserId(token: String): Long {
        return getClaims(token)["userId"]
            .toString()
            .toLong()
    }


}