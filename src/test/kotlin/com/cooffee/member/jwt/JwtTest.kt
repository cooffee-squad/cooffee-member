package com.cooffee.member.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.cooffee.member.common.BaseSpringBootTest
import com.cooffee.member.common.jwt.JwtDecoder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.util.*


class JwtTest: BaseSpringBootTest() {

    @Test
    fun decodeTest() {

        val secret = "my-secret"

        val token = JWT.create()
            .withIssuer("issuer")
            .withSubject("subject")
            .withIssuedAt(Date())
            .withExpiresAt(Date(Date().time + 3600 * 1000))
            .withClaim("userId", 1L)
            .withClaim("email", "test@test.com")
            .withClaim("name", "테스트")
            .sign(Algorithm.HMAC256(secret))

        val decode = JwtDecoder.verify(token, secret)

        with(decode) {
            val userId = claims["userId"]!!.asLong()
            assertEquals(userId, 1L)
        }
    }
}