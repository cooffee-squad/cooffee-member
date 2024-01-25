package com.cooffee.member.service

import com.cooffee.member.common.jwt.JwtProperties
import com.cooffee.member.common.jwt.JwtUtil
import com.cooffee.member.config.ServiceTest
import com.cooffee.member.exception.CustomException
import com.cooffee.member.model.SignInModel
import com.cooffee.member.model.SignUpModel
import com.cooffee.member.repository.MemberRepository
import com.cooffee.member.repository.redis.ConfirmTokenRepository
import com.cooffee.member.repository.redis.RefreshTokenRepository
import com.cooffee.member.util.MailUtil
import com.ninjasquad.springmockk.MockkBean
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.extensions.Extension
import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import org.springframework.boot.SpringApplicationRunListener
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.PostgreSQLContainer

@ServiceTest
class MemberServiceTest(
    @MockkBean private val mailUtil: MailUtil,
    private val memberRepository: MemberRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val confirmTokenRepository: ConfirmTokenRepository,
    private val jwtUtil: JwtUtil,
    private val jwtProperties: JwtProperties,
    private val passwordEncoder: PasswordEncoder,
) : BehaviorSpec({

    val memberService: MemberService = MemberServiceImpl(memberRepository, refreshTokenRepository, confirmTokenRepository, mailUtil, jwtUtil, jwtProperties, passwordEncoder)

    given("멤버가 가입할 때") {
        every { mailUtil.sendMail(any(), any()) } just Runs
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
            val exception = shouldThrow<CustomException> {
                memberService.getMemberByEmail(email)
            }
            then("예외를 반환한다") {
                exception.message shouldContain  "회원을 찾을 수 없습니다."
            }
        }
    }

    given("가입된 멤버가") {
        val normalSignInModel = SignInModel(
            email = "dummy1@test.com",
            password = "123",
        )
        val abnormalSignInModel = SignInModel(
            email = "dummy1@test.com",
            password = "1234",
        )
        val unConfirmSignInModel = SignInModel(
            email = "dummy2@test.com",
            password = "123",
        )
        `when`("로그인 할 때") {
            val token = memberService.signIn(normalSignInModel)
            then("토큰을 반환한다") {
                token shouldNotBe null
            }
        }
        `when`("패스워드가 틀리면") {
            val exception = shouldThrow<CustomException> {
                memberService.signIn(abnormalSignInModel)
            }
            then("예외를 반환한다") {
                exception.message shouldContain "패스워드가 일치하지 않습니다."
            }
        }
        `when`("이메일 인증이 되지 않으면") {
            val exception = shouldThrow<CustomException> {
                memberService.signIn(unConfirmSignInModel)
            }
            then("예외를 반환한다") {
                exception.message shouldContain "이메일 인증이 되지 않은 회원입니다."
            }
        }
    }

    //Spec 종료 후 컨테이너를 명시적으로 중지
    afterSpec {
        container.stop()
        redisContainer.stop()
    }

}) {

    override fun extensions(): List<Extension> = listOf(SpringExtension)

    companion object {
        private val container: PostgreSQLContainer<*> = PostgreSQLContainer<Nothing>("postgres:latest").apply {
            withDatabaseName("testdb")
            withUsername("testuser")
            withPassword("testpassword")
            withInitScript("db/init.sql")
        }.also { it.start() }

        private val redisContainer: GenericContainer<*> = GenericContainer<Nothing>("redis:latest").apply {
            withExposedPorts(6379)
        }.also { it.start() }

        @JvmStatic
        @DynamicPropertySource
        fun overrideProps(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", container::getJdbcUrl)
            registry.add("spring.datasource.username", container::getUsername)
            registry.add("spring.datasource.password", container::getPassword)
            registry.add("spring.data.redis.host", redisContainer::getContainerIpAddress)
            registry.add("spring.data.redis.port", redisContainer::getFirstMappedPort)
        }
    }
}