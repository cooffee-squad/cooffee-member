package com.cooffee.member.config

import com.cooffee.member.service.OAuthService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class WebConfig
    (
    private val oAuthService: OAuthService
) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { it.disable() }
            .authorizeHttpRequests { authorize ->
                authorize.requestMatchers("/v1/member/**").permitAll()
                    .anyRequest().authenticated()
            }
            .formLogin { formLogin ->
                formLogin.disable()
            }
            .oauth2Login { it ->
                it.loginProcessingUrl("/v1/member/oauth2/authorization")
                it.userInfoEndpoint {
                    it.userService(oAuthService)
                }
            }
        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}
