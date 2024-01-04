package com.cooffee.member.jwt

import com.cooffee.member.common.jwt.JwtClaim
import com.cooffee.member.common.jwt.JwtProperties
import com.cooffee.member.common.jwt.JwtUtil
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class JwtTest(
    private val jwtUtil: JwtUtil,
    private val jwtProperties: JwtProperties,
) : BehaviorSpec({

    given("토큰이 주어졌을 때") {
        val claim = JwtClaim(1L, "test@test.com", "테스트")
        val token = jwtUtil.createToken(claim, jwtProperties)

        `when`("토큰을 검증한 후") {
            val decode = jwtUtil.verify(token, jwtProperties.secret)

            then("claim을 가져온다") {
                with(decode) {
                    claims["memberId"]?.asLong() shouldBe 1L
                    claims["email"]?.asString() shouldBe "test@test.com"
                    claims["name"]?.asString() shouldBe "테스트"
                }
            }
        }
    }
})