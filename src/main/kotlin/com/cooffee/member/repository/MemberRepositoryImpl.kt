package com.cooffee.member.repository

import com.cooffee.member.domain.Member
import org.springframework.stereotype.Repository

@Repository
class MemberRepositoryImpl(
    private val memberJpaRepository: MemberJpaRepository
) : MemberRepository {

    override fun findByEmail(email: String): Member? {
        return memberJpaRepository.findByEmail(email)
    }

    override fun save(member: Member): Member {
        return memberJpaRepository.save(member)
    }
}