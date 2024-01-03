package com.cooffee.member.service

import com.cooffee.member.common.jwt.JwtUtil
import com.cooffee.member.common.jwt.JwtProperties
import com.cooffee.member.config.ServiceTest
import com.cooffee.member.model.SignInModel
import com.cooffee.member.model.SignUpModel
import com.cooffee.member.repository.MemberRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer

@ServiceTest
class MemberServiceTest(
    private val memberRepository: MemberRepository,
    private val jwtUtil: JwtUtil,
    private val jwtProperties: JwtProperties,
    private val passwordEncoder: PasswordEncoder,
) : BehaviorSpec({

    val memberService: MemberService = MemberServiceImpl(memberRepository, jwtUtil, jwtProperties, passwordEncoder)

    given("멤버가 가입할 때") {
        val member = SignUpModel(
            name = "test",
            email = "test@test.com",
            password = "123",
            phone = "010-1234-5678",
            mainAddress = "경기도 성남시 분당구",
            subAddress = "코코아빌딩 1층",
            zipcode = 54321,
        )
        `when`("멤버를 저장 후") {
            val member = memberService.signUp(member)
            then("저장한 멤버의 아이디를 리턴한다") {
                member.name shouldBe "test"
            }
        }
    }

    given("멤버를 조회할 때") {
        val email: String = "no@test.com"
        `when`("일치하는 이메일이 없으면") {
            val exception = shouldThrow<RuntimeException> {
                memberService.findByEmail(email)
            }
            then("예외를 반환한다") {
                exception.message shouldBe "존재하지 않는 멤버입니다"
            }
        }
    }

    given("가입된 멤버가") {
        val normalSignInModel = SignInModel(
            email = "dummy@test.com",
            password = "123",
        )
        val abnormalSignInModel = SignInModel(
            email = "dummy@test.com",
            password = "1234",
        )
        `when`("로그인 할 때") {
            val token = memberService.signIn(normalSignInModel)
            then("토큰을 반환한다") {
                token shouldNotBe null
            }
        }
        `when`("패스워드가 틀리면") {
            val exception = shouldThrow<RuntimeException> {
                memberService.signIn(abnormalSignInModel)
            }
            then("예외를 반환한다") {
                exception.message shouldBe "패스워드가 일치하지 않습니다"
            }
        }
    }

    //Spec 종료 후 컨테이너를 명시적으로 중지
    afterSpec { container.stop() }

}) {

    companion object {
        private val container: PostgreSQLContainer<*> = PostgreSQLContainer<Nothing>("postgres:latest").apply {
            withDatabaseName("testdb")
            withUsername("testuser")
            withPassword("testpassword")
            withInitScript("db/init.sql")
        }.also { it.start() }

        @JvmStatic
        @DynamicPropertySource
        fun overrideProps(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", container::getJdbcUrl)
            registry.add("spring.datasource.username", container::getUsername)
            registry.add("spring.datasource.password", container::getPassword)
        }
    }
}