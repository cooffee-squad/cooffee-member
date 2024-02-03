package com.cooffee.member.controller

import com.cooffee.member.model.SignInModel
import com.cooffee.member.model.SignUpModel
import com.fasterxml.jackson.databind.ObjectMapper
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
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.queryParameters
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.PostgreSQLContainer
import javax.xml.xpath.XPathConstants.NUMBER
import javax.xml.xpath.XPathConstants.STRING

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
            .andDo (
                document(
                    "member-details",
                    requestHeaders(
                        headerWithName("Authorization").description("인증 토큰"),
                    ),
                    queryParameters(
                        parameterWithName("email").description("회원 이메일"),
                    ),
                    responseFields(
                        fieldWithPath("statusCode").type(NUMBER).description("상태 코드"),
                        fieldWithPath("status").type(STRING).description("상태"),
                        fieldWithPath("data.email").type(STRING).description("회원 이메일"),
                        fieldWithPath("data.name").type(STRING).description("회원 이름"),
                        fieldWithPath("data.phone").type(STRING).description("회원 전화번호"),
                        fieldWithPath("data.address").type(STRING).description("회원 주소"),
                        fieldWithPath("data.address.mainAddress").type(STRING).description("회원 주소(도로명)"),
                        fieldWithPath("data.address.subAddress").type(STRING).description("회원 주소(상세)"),
                        fieldWithPath("data.address.zipcode").type(NUMBER).description("회원 주소(우편번호)"),
                    ),
                ),
            )
    }

    @Test
    @DisplayName("회원 가입")
    fun signUp() {
        // given
        val signUpModel = SignUpModel(
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
            .andDo (
                document(
                    "member-sign-up",
                    requestHeaders(
                        headerWithName("Authorization").description("인증 토큰"),
                    ),
                    requestFields(
                        fieldWithPath("email").type(STRING).description("회원 이메일"),
                        fieldWithPath("password").type(STRING).description("회원 비밀번호"),
                        fieldWithPath("name").type(STRING).description("회원 이름"),
                        fieldWithPath("phone").type(STRING).description("회원 전화번호"),
                        fieldWithPath("mainAddress").type(STRING).description("회원 주소(도로명)"),
                        fieldWithPath("subAddress").type(STRING).description("회원 주소(상세)"),
                        fieldWithPath("zipcode").type(NUMBER).description("회원 주소(우편번호)"),
                    ),
                    responseFields(
                        fieldWithPath("statusCode").type(NUMBER).description("상태 코드"),
                        fieldWithPath("status").type(STRING).description("상태"),
                        fieldWithPath("data.email").type(STRING).description("회원 이메일"),
                        fieldWithPath("data.name").type(STRING).description("회원 이름"),
                    ),
                ),
            )
    }

    @Test
    @DisplayName("회원 로그인")
    fun signIn() {
        // given
        val signInModel = SignInModel(
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
                    objectMapper.writeValueAsString(signInModel)
                ),
        )
            .andExpect { status().isOk }
            .andDo { print() }
            .andDo (
                document(
                    "member-sign-in",
                    requestHeaders(
                        headerWithName("Authorization").description("인증 토큰"),
                    ),
                    requestFields(
                        fieldWithPath("email").type(STRING).description("회원 이메일"),
                        fieldWithPath("password").type(STRING).description("회원 비밀번호"),
                    ),
                    responseFields(
                        fieldWithPath("statusCode").type(NUMBER).description("상태 코드"),
                        fieldWithPath("status").type(STRING).description("상태"),
                        fieldWithPath("data.accessToken").type(STRING).description("회원 토큰"),
                        fieldWithPath("data.refreshToken").type(STRING).description("회원 리프레시 토큰"),
                    ),
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
