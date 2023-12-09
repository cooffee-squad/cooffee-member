package com.cooffee.member.service

import com.cooffee.member.domain.Member

interface MemberService {
    fun signUp() : Member
}