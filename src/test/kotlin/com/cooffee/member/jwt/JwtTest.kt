package com.cooffee.member.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.cooffee.member.common.jwt.JwtDecoder
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestConstructor
import java.util.*

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class JwtTest(private val jwtDecoder: JwtDecoder): BehaviorSpec({

    given("토큰이 주어졌을 때") {
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

        `when`("토큰을 검증한 후") {
            val decode = jwtDecoder.verify(token, secret)

            then("claim을 가져온다") {
                with(decode) {
                    claims["userId"]?.asLong() shouldBe 1L
                    claims["email"]?.asString() shouldBe "test@test.com"
                    claims["name"]?.asString() shouldBe "테스트"
                }
            }
        }
    }
})