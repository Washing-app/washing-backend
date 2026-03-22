package com.washingapp.washing_backend.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.SecretKey

@Service
class JwtService(
    @Value("\${jwt.secret}")
    private val secret: String
) {

    private val secretKey: SecretKey = Keys.hmacShaKeyFor(secret.toByteArray())

    fun generateToken(userId: UUID): String {
        return Jwts.builder()
            .setSubject(userId.toString())
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 час
            .signWith(secretKey)
            .compact()
    }

    fun extractUserId(token: String): UUID {
        val claims = Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)

        return UUID.fromString(claims.body.subject)
    }
}