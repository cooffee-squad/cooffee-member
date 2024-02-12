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
import java.util.Date

private val log = LogManager.getLogger()

@Component
class JwtUtil {
    fun createAccessToken(
        jwtClaim: JwtClaim,
        properties: JwtProperties,
    ): String =
        "Bearer " +
            JWT.create()
                .withIssuer(properties.issuer)
                .withSubject(properties.subject)
                .withIssuedAt(Date())
                .withExpiresAt(Date(Date().time + properties.expiresTime * 1000))
                .withClaim("email", jwtClaim.email)
                .withClaim("name", jwtClaim.name)
                .sign(Algorithm.HMAC256(properties.secret))

    fun createRefreshToken(
        jwtClaim: JwtClaim,
        properties: JwtProperties,
    ): String =
        JWT.create()
            .withIssuer(properties.issuer)
            .withSubject(properties.subject)
            .withIssuedAt(Date())
            .withExpiresAt(Date(Date().time + properties.expiresTime * 1000))
            .sign(Algorithm.HMAC256(properties.secret))

    fun verify(
        token: String,
        secret: String,
    ): DecodedJWT {
        val algorithm = Algorithm.HMAC256(secret)
        val verifier: JWTVerifier = JWT.require(algorithm).build()

        return try {
            verifier.verify(token.substring(7))
        } catch (e: JWTVerificationException) {
            log.error("유효하지 않은 토큰입니다 : {}", e.message)
            throw CustomException(ExceptionType.UNAUTHORIZED)
        }
    }

    fun verify(token: String): Boolean {
        return true
    }

    fun getMemberFromToken(token: String): JwtClaim {
        val decodedJWT = JWT.decode(token)
        return JwtClaim(
            email = decodedJWT.getClaim("email").asString(),
            name = decodedJWT.getClaim("name").asString(),
        )
    }
}

data class JwtClaim(
    val email: String,
    val name: String,
)
