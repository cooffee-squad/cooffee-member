package com.cooffee.member.repository

import com.cooffee.member.domain.Member

interface MemberRepository {
    fun save(member: Member): Member

    fun findByEmail(email: String): Member?

    fun findAll(): List<Member>
}
