package com.cooffee.member.config

import com.cooffee.member.common.CustomOAuth2SuccessHandler
import com.cooffee.member.common.jwt.JwtAuthenticationFilter
import com.cooffee.member.service.CustomOAuth2Service
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class WebConfig(
    private val customOAuth2Service: CustomOAuth2Service,
    private val customOAuth2SuccessHandler: CustomOAuth2SuccessHandler,
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { it.disable() }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authorizeHttpRequests {
                it.requestMatchers("/v1/member/signUp", "/v1/member/signIn").permitAll()
                    .anyRequest().authenticated()
            }
            .oauth2Login {
                it.authorizationEndpoint { endPoint ->
                    endPoint.baseUri("/v1/member/oauth2/login")
                }
                it.userInfoEndpoint { endPoint ->
                    endPoint.userService(customOAuth2Service)
                }
                it.successHandler(customOAuth2SuccessHandler)
            }
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}
