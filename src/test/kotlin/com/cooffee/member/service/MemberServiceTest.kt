package com.cooffee.member.service

import com.cooffee.member.domain.Member
import com.cooffee.member.enums.MemberType
import com.cooffee.member.repository.MemberRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.assertj.core.api.Assertions.*

import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class MemberServiceTest(
    private val memberRepository: MemberRepository,
) : BehaviorSpec({
    val memberService: MemberService = MemberServiceImpl(memberRepository)

    given("멤버가 가입할 때") {
        val member = Member(
            name = "test",
            email = "test@test.com",
            password = "123",
            phone = "010-1234-5678",
            type = MemberType.NORMAL,
        )
        `when`("멤버를 저장 후") {
            val memberName = memberService.signUp(member)
            then("저장한 멤버의 아이디를 리턴한다") {
                memberName shouldBe "test"
            }
        }
    }
})