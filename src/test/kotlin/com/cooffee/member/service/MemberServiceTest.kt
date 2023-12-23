package com.cooffee.member.service

import com.cooffee.member.common.BaseSpringBootTest
import com.cooffee.member.domain.Member
import com.cooffee.member.enums.MemberType
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container

class MemberServiceTest: BaseSpringBootTest() {

    @Autowired
    lateinit var memberService: MemberService

    @Test
    fun signUn() {
        val member = Member(
            name = "test",
            email = "test@test.com",
            password = "123",
            phone = "010-1234-5678",
            type = MemberType.NORMAL,
        )
        val memberName = memberService.signUp(member)
        assertThat(memberName).isEqualTo("test")
    }
}