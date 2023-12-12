package com.cooffee.member.controller

import com.cooffee.member.domain.Member
import com.cooffee.member.service.MemberService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/member")
class MemberController(
    private val memberService: MemberService,
) {

    @PostMapping
    fun singUp() {
        val memberName = memberService.signUp()
        println("memberName = $memberName")
    }

    @GetMapping
    fun getMember() {
        val id = 1L
        var findMember: Member = memberService.findById(id);
        println("find_email = ${findMember.email}")
        println("find_name = ${findMember.name}")
    }
}