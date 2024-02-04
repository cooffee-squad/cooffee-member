package com.cooffee.member.controller

import com.cooffee.member.common.NUMBER
import com.cooffee.member.common.STRING
import com.cooffee.member.common.andDocument
import com.cooffee.member.common.headerMeans
import com.cooffee.member.common.paramMeans
import com.cooffee.member.common.queryParameters
import com.cooffee.member.common.requestBody
import com.cooffee.member.common.requestHeaders
import com.cooffee.member.common.responseBody
import com.cooffee.member.common.type
import com.cooffee.member.model.SignInModel
import com.cooffee.member.model.SignUpModel
import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.assertions.print.print
import org.apache.logging.log4j.LogManager
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.PostgreSQLContainer

const val TOKEN = "local_token"
private val log = LogManager.getLogger()

@AutoConfigureRestDocs
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension::class)
@SpringBootTest
class MemberControllerTest {
    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    @DisplayName("회원 상세 조회")
    fun getMemberDetails() {
        // given
        // when
        // then
        mockMvc.perform(
            get("/v1/member/details")
                .param("email", "dummy1@test.com")
                .header("Authorization", TOKEN)
                .contentType("application/json"),
        )
            .andExpect { status().isOk }
            .andDo { print() }
            .andDocument(
                "member-details",
                requestHeaders(
                    "Authorization" headerMeans "인증 토큰",
                ),
                queryParameters(
                    "email" paramMeans "회원 이메일",
                ),
                responseBody(
                    "statusCode" type NUMBER means "상태 코드",
                    "status" type STRING means "상태",
                    "data.email" type STRING means "회원 이메일",
                    "data.name" type STRING means "회원 이름",
                    "data.phone" type STRING means "회원 전화번호",
                    "data.address.mainAddress" type STRING means "회원 주소(도로명)",
                    "data.address.subAddress" type STRING means "회원 주소(상세)",
                    "data.address.zipcode" type NUMBER means "회원 주소(우편번호)",
                ),
            )
    }

    @Test
    @DisplayName("회원 가입")
    fun signUp() {
        // given
        val signUpModel =
            SignUpModel(
                email = "test@test.com",
                password = "123",
                name = "test",
                phone = "010-1234-5678",
                mainAddress = "경기도 성남시 분당구",
                subAddress = "코코아빌딩 1층",
                zipcode = 54321,
            )
        // when
        // then
        mockMvc.perform(
            post("/v1/member/signUp")
                .header("Authorization", TOKEN)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(signUpModel)),
        )
            .andExpect { status().isOk }
            .andDo { print() }
            .andDocument(
                "member-sign-up",
                requestHeaders(
                    "Authorization" headerMeans "인증 토큰",
                ),
                requestBody(
                    "email" type STRING means "회원 이메일",
                    "password" type STRING means "회원 비밀번호",
                    "name" type STRING means "회원 이름",
                    "phone" type STRING means "회원 전화번호",
                    "mainAddress" type STRING means "회원 주소(도로명)",
                    "subAddress" type STRING means "회원 주소(상세)",
                    "zipcode" type NUMBER means "회원 주소(우편번호)",
                ),
                responseBody(
                    "statusCode" type NUMBER means "상태 코드",
                    "status" type STRING means "상태",
                    "data.email" type STRING means "회원 이메일",
                    "data.name" type STRING means "회원 이름",
                ),
            )
    }

    @Test
    @DisplayName("회원 로그인")
    fun signIn() {
        // given
        val signInModel =
            SignInModel(
                email = "dummy1@test.com",
                password = "123",
            )
        // when
        // then
        mockMvc.perform(
            post("/v1/member/signIn")
                .header("Authorization", TOKEN)
                .contentType("application/json")
                .content(
                    objectMapper.writeValueAsString(signInModel),
                ),
        )
            .andExpect { status().isOk }
            .andDo { print() }
            .andDocument(
                "member-sign-in",
                requestHeaders(
                    "Authorization" headerMeans "인증 토큰",
                ),
                requestBody(
                    "email" type STRING means "회원 이메일",
                    "password" type STRING means "회원 비밀번호",
                ),
                responseBody(
                    "statusCode" type NUMBER means "상태 코드",
                    "status" type STRING means "상태",
                    "data.accessToken" type STRING means "회원 토큰",
                    "data.refreshToken" type STRING means "회원 리프레시 토큰",
                ),
            )
    }

    companion object {
        private val container: PostgreSQLContainer<*> =
            PostgreSQLContainer<Nothing>("postgres:latest").apply {
                withDatabaseName("testdb")
                withUsername("testuser")
                withPassword("testpassword")
                withInitScript("db/init.sql")
            }.also { it.start() }

        private val redisContainer: GenericContainer<*> =
            GenericContainer<Nothing>("redis:latest").apply {
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

        @JvmStatic
        @AfterAll
        fun afterAll() {
            container.stop()
            redisContainer.stop()
        }
    }
}
