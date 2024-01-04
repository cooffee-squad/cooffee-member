package com.cooffee.member.repository

import com.cooffee.member.domain.Member
import org.springframework.stereotype.Repository

@Repository
class MemberRepositoryImpl(
    private val memberJpaRepository: MemberJpaRepository
) : MemberRepository {
    override fun save(member: Member): Member = memberJpaRepository.save(member)

    override fun findByEmail(email: String): Member? = memberJpaRepository.findByEmail(email)

    override fun findAll(): List<Member> = memberJpaRepository.findAll()
}