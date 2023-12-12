package com.cooffee.member.repository

import com.cooffee.member.domain.Member
import org.apache.logging.log4j.LogManager
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import java.lang.RuntimeException

private val log = LogManager.getLogger()

@Repository
class MemberRepositoryImpl(
    private val memberJpaRepository: MemberJpaRepository
) : MemberRepository {

    override fun findById(id: Long): Member {
        return memberJpaRepository.findByIdOrNull(id) ?: run {
            log.error("사용자가 없네요")
            throw RuntimeException("예외입니다")
        }
    }

    override fun save(member: Member): Member {
        return memberJpaRepository.save(member)
    }
}