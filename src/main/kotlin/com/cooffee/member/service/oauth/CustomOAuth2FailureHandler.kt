package com.cooffee.member.service.oauth

import com.cooffee.member.model.common.BasicResponse
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler
import org.springframework.stereotype.Component

@Component
class CustomOAuth2FailureHandler : SimpleUrlAuthenticationFailureHandler() {
    override fun onAuthenticationFailure(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        exception: AuthenticationException?,
    ) {
        val objectMapper = jacksonObjectMapper()
        response?.apply {
            contentType = MediaType.APPLICATION_JSON_VALUE
            status = HttpServletResponse.SC_UNAUTHORIZED
            val message = mapOf("message" to "Authentication failed")
            val apiResponse = BasicResponse.toResponse(HttpStatus.NOT_FOUND, message)
            val messageJson = objectMapper.writeValueAsString(apiResponse)
            writer.write(messageJson)
        }
    }
}
