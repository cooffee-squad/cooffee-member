package com.cooffee.member.controller

import com.cooffee.member.service.MemberService
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
        memberService.signUp()
    }
}