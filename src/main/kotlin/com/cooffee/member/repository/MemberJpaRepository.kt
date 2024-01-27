package com.cooffee.member.repository

import com.cooffee.member.domain.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberJpaRepository : JpaRepository<Member, Long> {
    fun findByEmail(email: String): Member?
}
