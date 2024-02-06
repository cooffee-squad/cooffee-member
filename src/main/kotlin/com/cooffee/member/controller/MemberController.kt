package com.cooffee.member.controller

import com.cooffee.member.domain.Member
import com.cooffee.member.model.MemberResponse
import com.cooffee.member.model.SignInModel
import com.cooffee.member.model.SignInResponse
import com.cooffee.member.model.SignUpModel
import com.cooffee.member.model.SignUpResponse
import com.cooffee.member.model.common.BasicResponse
import com.cooffee.member.service.MemberService
import jakarta.servlet.http.HttpServletResponse
import org.apache.logging.log4j.LogManager
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

private val log = LogManager.getLogger()

@RestController
@RequestMapping("/v1/member")
class MemberController(
    private val memberService: MemberService,
) {
    @PostMapping("/signUp")
    fun singUp(
        @RequestBody signUpModel: SignUpModel,
    ): BasicResponse<SignUpResponse> {
        log.info("Sign Up Member email : {}", signUpModel.email)
        val member = memberService.signUp(signUpModel)
        return BasicResponse.toResponse(HttpStatus.CREATED, SignUpResponse(member.email, member.name))
    }

    @PostMapping("/signIn")
    fun signIn(
        @RequestBody signInModel: SignInModel,
    ): BasicResponse<SignInResponse> {
        log.info("Member {} login", signInModel.email)
        val signInResponse = memberService.signIn(signInModel)
        return BasicResponse.toResponse(HttpStatus.OK, signInResponse)
    }

    @GetMapping("/details")
    fun memberDetail(
        @RequestHeader("authorization") authorization: String,
        @RequestParam("email") email: String,
    ): BasicResponse<MemberResponse> {
        log.info("Get Member Details : {}", email)
        val member: Member = memberService.getMemberByEmail(email)
        val memberResponse =
            MemberResponse(
                email = member.email,
                name = member.name,
                phone = member.phone,
                address = member.address,
            )
        return BasicResponse.toResponse(HttpStatus.OK, memberResponse)
    }

    @GetMapping("/confirm-mail")
    fun confirmMail(
        @RequestParam email: String,
        @RequestParam token: String,
    ): BasicResponse<String> {
        return BasicResponse.toResponse(HttpStatus.OK, memberService.activateMember(email, token))
    }

    @GetMapping("/oauth2")
    fun oAuthLogin(
    ) {
    }
}
