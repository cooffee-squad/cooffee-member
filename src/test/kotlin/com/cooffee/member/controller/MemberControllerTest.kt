package com.cooffee.member.controller

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
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.queryParameters
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
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
            .andDo(print())
            .andDo(
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

    companion object {
        private val postgreSQLContainer: PostgreSQLContainer<*> =
            PostgreSQLContainer<Nothing>("postgres:latest").apply {
                withDatabaseName("testdb")
                withUsername("testuser")
                withPassword("testpassword")
                withInitScript("db/init.sql")
            }.also { it.start() }

        @JvmStatic
        @DynamicPropertySource
        fun overrideProps(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl)
            registry.add("spring.datasource.username", postgreSQLContainer::getUsername)
            registry.add("spring.datasource.password", postgreSQLContainer::getPassword)
        }

        @JvmStatic
        @AfterAll
        fun afterAll() {
            postgreSQLContainer.stop()
        }
    }
}
