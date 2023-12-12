package com.cooffee.member.repository

import com.cooffee.member.domain.Member

interface MemberRepository {
    fun findById(id : Long) : Member
    fun save(member: Member) : Member
}