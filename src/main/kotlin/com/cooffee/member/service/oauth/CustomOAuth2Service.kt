package com.cooffee.member.service.oauth

import com.cooffee.member.repository.MemberRepository
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class CustomOAuth2Service(
    private val memberRepository: MemberRepository,
) : OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    override fun loadUser(userRequest: OAuth2UserRequest?): OAuth2User {
        val authUser = DefaultOAuth2UserService().loadUser(userRequest)
        val userNameAttributeName =
            userRequest?.clientRegistration?.providerDetails?.userInfoEndpoint?.userNameAttributeName

        val email = authUser.attributes["email"] as String
        val member = memberRepository.findByEmail(email) ?: throw AuthenticationServiceException("Member not found")

        return DefaultOAuth2User(
            listOf(SimpleGrantedAuthority(member.type.name)),
            authUser.attributes,
            userNameAttributeName,
        )
    }
}
