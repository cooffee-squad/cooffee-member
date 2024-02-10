package com.cooffee.member.service

import com.cooffee.member.exception.CustomException
import com.cooffee.member.exception.ExceptionType
import com.cooffee.member.model.MemberDetails
import com.cooffee.member.repository.MemberRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val memberRepository: MemberRepository,
) : UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        val member =
            username?.let { memberRepository.findByEmail(it) } ?: throw CustomException(ExceptionType.MEMBER_NOT_FOUND)

        return MemberDetails(
            id = member.id,
            name = member.name,
            email = member.email,
            password = member.password,
            phone = member.phone,
            address = member.address,
            type = member.type,
            confirm = member.confirm,
        )
    }
}
