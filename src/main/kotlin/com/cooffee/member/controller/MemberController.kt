package com.cooffee.member.controller

import com.cooffee.member.domain.Member
import com.cooffee.member.model.*
import com.cooffee.member.model.common.BasicResponse
import com.cooffee.member.service.MemberService
import org.apache.logging.log4j.LogManager
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

private val log = LogManager.getLogger()

@RestController
@RequestMapping("/v1/member")
class MemberController(
    private val memberService: MemberService,
) {

    @PostMapping("/signUp")
    fun singUp(@RequestBody signUpModel: SignUpModel): BasicResponse<SignUpResponse> {
        log.info("Sign Up Member email : {}", signUpModel.email)
        val member = memberService.signUp(signUpModel)
        return BasicResponse.toResponse(HttpStatus.CREATED, SignUpResponse(member.email, member.name))
    }

    @PostMapping("/signIn")
    fun signIn(@RequestBody signInModel: SignInModel): BasicResponse<SignInResponse> {
        log.info("Member {} login", signInModel.email)
        memberService.signIn(signInModel)

        return BasicResponse.toResponse(HttpStatus.OK, SignInResponse("token"))
    }

    @GetMapping("/details")
    fun memberDetail(@RequestHeader("authorization") authorization: String,
                     @RequestParam("email") email: String): BasicResponse<MemberResponse> {
        log.info("Get Member Details : {}", email)
        val member: Member = memberService.findByEmail(email)
        val memberResponse = MemberResponse(
            email = member.email,
            name = member.name,
            phone = member.phone,
            address = member.address
        )
        return BasicResponse.toResponse(HttpStatus.OK, memberResponse)
    }
}