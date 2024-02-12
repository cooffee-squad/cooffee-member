package com.cooffee.member.service.oauth

import com.cooffee.member.common.jwt.JwtClaim
import com.cooffee.member.common.jwt.JwtProperties
import com.cooffee.member.common.jwt.JwtUtil
import com.cooffee.member.model.common.BasicResponse
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class CustomOAuth2SuccessHandler(
    private val jwtProperties: JwtProperties,
    private val jwtUtil: JwtUtil,
) : SimpleUrlAuthenticationSuccessHandler() {
    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication?,
    ) {
        val objectMapper = jacksonObjectMapper()
        val oAuth2User = authentication?.principal as OAuth2User

        val jwtOauth2Claim =
            JwtClaim(
                email = oAuth2User.attributes["email"] as String,
                name = oAuth2User.attributes["name"] as String,
            )

        val accessToken = jwtUtil.createAccessToken(jwtOauth2Claim, jwtProperties)
        val refreshToken = jwtUtil.createRefreshToken(jwtOauth2Claim, jwtProperties)

        response?.apply {
            contentType = MediaType.APPLICATION_JSON_VALUE
            status = HttpServletResponse.SC_OK
            val tokens =
                mapOf(
                    "access_token" to accessToken,
                    "refresh_token" to refreshToken,
                )
            val apiResponse = BasicResponse.toResponse(HttpStatus.OK, tokens)
            val tokenJson = objectMapper.writeValueAsString(apiResponse)
            writer.write(tokenJson)
        }
    }
}
