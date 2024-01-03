package com.cooffee.member.service

import com.cooffee.member.common.jwt.JwtClaim
import com.cooffee.member.common.jwt.JwtProperties
import com.cooffee.member.common.jwt.JwtUtil
import com.cooffee.member.domain.Address
import com.cooffee.member.domain.Member
import com.cooffee.member.enums.MemberType
import com.cooffee.member.model.SignInModel
import com.cooffee.member.model.SignUpModel
import com.cooffee.member.repository.MemberRepository
import org.apache.logging.log4j.LogManager
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

private val log = LogManager.getLogger()

@Service
@Transactional(readOnly = true)
class MemberServiceImpl(
    private val memberRepository: MemberRepository,
    private val jwtUtil: JwtUtil,
    private val jwtProperties: JwtProperties,
    private val encoder: PasswordEncoder,
) : MemberService {

    @Transactional
    override fun signUp(signUpModel: SignUpModel): Member {
        with(signUpModel) {
            memberRepository.findByEmail(email)?.let {
                log.error("Already Existed Email : {}", email)
                throw RuntimeException("이미 존재하는 멤버입니다")
            }
        }

        val member = Member(
            name = signUpModel.name,
            email = signUpModel.email,
            password = encoder.encode(signUpModel.password),
            phone = signUpModel.phone,
            address = Address(signUpModel.mainAddress, signUpModel.subAddress, signUpModel.zipcode),
            type = MemberType.NORMAL,
        )
        return memberRepository.save(member)
    }

    @Transactional
    override fun signIn(signInModel: SignInModel): String = with(signInModel) {

        val member = memberRepository.findByEmail(email) ?: throw RuntimeException("존재하지 않는 멤버입니다")

        encoder.matches(signInModel.password, member.password).takeIf { it }
            ?.let {
                val claim = JwtClaim(
                    id = member.id ?: throw RuntimeException("아이디가 없습니다"),
                    email = member.email,
                    name = member.name,
                )
                jwtUtil.createToken(claim, jwtProperties)
            } ?: throw RuntimeException("패스워드가 일치하지 않습니다")
    }

    override fun findByEmail(email: String): Member =
        memberRepository.findByEmail(email) ?: throw RuntimeException("존재하지 않는 멤버입니다")


    override fun findAllMember(): List<Member> = memberRepository.findAll()
}