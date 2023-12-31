package com.cooffee.member.service

import com.cooffee.member.common.jwt.JwtClaim
import com.cooffee.member.common.jwt.JwtProperties
import com.cooffee.member.common.jwt.JwtUtil
import com.cooffee.member.domain.Member
import com.cooffee.member.enums.MemberType
import com.cooffee.member.model.SignInModel
import com.cooffee.member.model.SignUpModel
import com.cooffee.member.repository.MemberRepository
import org.apache.logging.log4j.LogManager
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

private val log = LogManager.getLogger()

@Service
@Transactional(readOnly = true)
class MemberServiceImpl(
    private val memberRepository: MemberRepository,
    private val jwtUtil: JwtUtil,
    private val jwtProperties: JwtProperties,
) : MemberService {

    @Transactional
    override fun signUp(signUpModel: SignUpModel): Member {
        with(signUpModel) {
            memberRepository.findByEmail(email)?.let {
                throw RuntimeException("이미 존재하는 멤버입니다")
            }
        }

        val member = Member(
            name = signUpModel.name,
            email = signUpModel.email,
            password = signUpModel.password,
            phone = signUpModel.phone,
            type = MemberType.NORMAL,
        )
        return memberRepository.save(member)
    }

    @Transactional
    override fun signIn(signInModel: SignInModel): String {
        return with(signInModel) {
            val member = memberRepository.findByEmail(email) ?: throw RuntimeException("존재하지 않는 멤버입니다")

            val claim = JwtClaim(
                id = member.id ?: throw RuntimeException("아이디가 없습니다"),
                email = member.email,
                name = member.name,
                phone = member.phone,
            )
            jwtUtil.createToken(claim, jwtProperties)
        }
    }

    override fun findByEmail(email: String): Member {
        return memberRepository.findByEmail(email) ?: throw RuntimeException("존재하지 않는 멤버입니다")
    }

    override fun findAllMember(): List<Member> {
        return memberRepository.findAll()
    }
}