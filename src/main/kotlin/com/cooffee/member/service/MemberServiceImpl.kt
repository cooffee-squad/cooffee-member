package com.cooffee.member.service

import com.cooffee.member.domain.Member
import com.cooffee.member.enums.MemberType
import org.apache.logging.log4j.LogManager
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

private val log = LogManager.getLogger()
@Service
@Transactional
class MemberServiceImpl : MemberService {

    override fun signUp(): Member {
        log.info("Member signUp")
        return Member(1L, "Test", "test@test.com", "password", null, MemberType.ADMIN)
    }
}