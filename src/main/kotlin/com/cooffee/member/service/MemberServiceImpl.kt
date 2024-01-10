package com.cooffee.member.service

import com.cooffee.member.common.jwt.JwtClaim
import com.cooffee.member.common.jwt.JwtProperties
import com.cooffee.member.common.jwt.JwtUtil
import com.cooffee.member.domain.Address
import com.cooffee.member.domain.Member
import com.cooffee.member.enums.MemberType
import com.cooffee.member.exception.CustomException
import com.cooffee.member.exception.ExceptionType
import com.cooffee.member.model.SignInModel
import com.cooffee.member.model.SignUpModel
import com.cooffee.member.repository.MemberRepository
import com.cooffee.member.util.MailUtil
import org.apache.logging.log4j.LogManager
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

private val log = LogManager.getLogger()

@Service
@Transactional(readOnly = true)
class MemberServiceImpl(
    private val memberRepository: MemberRepository,
    private val mailUtil: MailUtil,
    private val jwtUtil: JwtUtil,
    private val jwtProperties: JwtProperties,
    private val encoder: PasswordEncoder,
) : MemberService {

    @Transactional
    override fun signUp(signUpModel: SignUpModel): Member {
        with(signUpModel) {
            memberRepository.findByEmail(email)?.let {
                log.error("Already Existed Email : {}", email)
                throw CustomException(ExceptionType.ALREADY_EXISTED_MEMBER)
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

        val result = mailUtil.sendMail(signUpModel.email)
        println("result = ${result}")

        return memberRepository.save(member)
    }

    @Transactional
    override fun signIn(signInModel: SignInModel): String = with(signInModel) {

        val member = memberRepository.findByEmail(email) ?: throw CustomException(ExceptionType.MEMBER_NOT_FOUND)

        encoder.matches(signInModel.password, member.password).takeIf { it }
            ?.let {
                val claim = JwtClaim(
                    id = member.id ?: throw CustomException(ExceptionType.MEMBER_ID_NOT_FOUND),
                    email = member.email,
                    name = member.name,
                )
                jwtUtil.createToken(claim, jwtProperties)
            } ?: throw CustomException(ExceptionType.INCORRECT_PASSWORD)
    }

    override fun findByEmail(email: String): Member =
        memberRepository.findByEmail(email) ?: throw CustomException(ExceptionType.MEMBER_NOT_FOUND)


    override fun findAllMember(): List<Member> = memberRepository.findAll()
}