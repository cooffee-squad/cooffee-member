package com.cooffee.member.common.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import com.auth0.jwt.interfaces.JWTVerifier
import com.cooffee.member.exception.CustomException
import com.cooffee.member.exception.ExceptionType
import org.apache.logging.log4j.LogManager
import org.springframework.stereotype.Component
import java.util.*

private val log = LogManager.getLogger()

@Component
class JwtUtil {

    fun createAccessToken(jwtClaim: JwtClaim, properties: JwtProperties): String =
        JWT.create()
            .withIssuer(properties.issuer)
            .withSubject(properties.subject)
            .withIssuedAt(Date())
            .withExpiresAt(Date(Date().time + properties.expiresTime * 1000))
            .withClaim("memberId", jwtClaim.id)
            .withClaim("email", jwtClaim.email)
            .withClaim("name", jwtClaim.name)
            .sign(Algorithm.HMAC256(properties.secret))

    fun createRefreshToken(jwtClaim: JwtClaim, properties: JwtProperties): String =
        JWT.create()
            .withIssuer(properties.issuer)
            .withSubject(properties.subject)
            .withIssuedAt(Date())
            .withExpiresAt(Date(Date().time + properties.expiresTime * 1000))
            .sign(Algorithm.HMAC256(properties.secret))

    fun verify(token: String, secret: String): DecodedJWT {
        val algorithm = Algorithm.HMAC256(secret)
        val verifier: JWTVerifier = JWT.require(algorithm).build()

        return try {
            verifier.verify(token)
        } catch (e: JWTVerificationException) {
            log.error("유효하지 않은 토큰입니다 : {}", e.message)
            throw CustomException(ExceptionType.UNAUTHORIZED)
        }
    }
}

data class JwtClaim(
    val id: Long,
    val email: String,
    val name: String,
)