package com.cooffee.member.common.jwt

import com.cooffee.member.service.CustomMemberDetailsService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

const val AUTHORIZATION = "Authorization"

@Component
class JwtAuthenticationFilter(
    private val customMemberDetailsService: CustomMemberDetailsService,
    private val jwtUtil: JwtUtil,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        getTokenInfo(request)?.let { token ->
            if (jwtUtil.verify(token)) {
                jwtUtil.getMemberFromToken(token).email.let { email ->
                    customMemberDetailsService.loadUserByUsername(email).run {
                        val authentication = UsernamePasswordAuthenticationToken(this, null, authorities)
                        SecurityContextHolder.getContext().authentication = authentication
                    }
                }
            }
        }
        filterChain.doFilter(request, response)
    }

    private fun getTokenInfo(request: HttpServletRequest): String? {
        return request.getHeader(AUTHORIZATION)?.takeIf { it.startsWith("Bearer ") }?.substring(7)
    }
}
