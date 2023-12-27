package com.cooffee.member.repository

import com.cooffee.member.domain.Member

interface MemberRepository {
    fun findByEmail(email: String) : Member?
    fun save(member: Member) : Member
}