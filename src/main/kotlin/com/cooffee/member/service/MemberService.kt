package com.cooffee.member.service

import com.cooffee.member.domain.Member
import com.cooffee.member.model.SignInModel
import com.cooffee.member.model.SignInResponse
import com.cooffee.member.model.SignUpModel

interface MemberService {
    fun signUp(signUpModel: SignUpModel): Member
    fun signIn(signInModel: SignInModel): SignInResponse
    fun getMemberByEmail(email: String): Member
    fun findAllMember(): List<Member>
    fun confirmMember(email: String, token: String): String
}