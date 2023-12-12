package com.cooffee.member.service

import com.cooffee.member.domain.Member
import com.cooffee.member.enums.MemberType
import com.cooffee.member.repository.MemberRepository
import org.apache.logging.log4j.LogManager
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

private val log = LogManager.getLogger()
@Service
@Transactional
class MemberServiceImpl(
    private val memberRepository: MemberRepository
) : MemberService {

    @Transactional
    override fun signUp(): String {
        log.info("Member signUp")
        val member = Member(
            name = "Test",
            email = "test@test.com",
            password = "password",
            phone = "010-1234-5678",
            type = MemberType.ADMIN)
        return memberRepository.save(member).name
    }

    @Transactional(readOnly = true)
    override fun findById(id: Long): Member {
        return memberRepository.findById(id);
    }
}