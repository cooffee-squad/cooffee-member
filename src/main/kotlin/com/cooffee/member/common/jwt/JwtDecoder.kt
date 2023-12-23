package com.cooffee.member.common.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import com.auth0.jwt.interfaces.JWTVerifier
import org.slf4j.LoggerFactory

private val log = LoggerFactory.getLogger(JwtDecoder::class.java)

object JwtDecoder {
    fun verify(token: String, secret: String) : DecodedJWT {
        val algorithm = Algorithm.HMAC256(secret)
        val verifier: JWTVerifier = JWT.require(algorithm).build()

        return try {
            verifier.verify(token)
        } catch (e: JWTVerificationException) {
            log.error("유효하지 않은 토큰입니다 : {}", e.message)
            TODO("공통 예외처리 필요")
            throw RuntimeException("유효하지 않은 토큰입니다.")
        }
    }
}