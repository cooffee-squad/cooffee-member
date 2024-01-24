package com.cooffee.member.service

import com.cooffee.member.common.jwt.JwtClaim
import com.cooffee.member.common.jwt.JwtProperties
import com.cooffee.member.common.jwt.JwtUtil
import com.cooffee.member.domain.Address
import com.cooffee.member.domain.ConfirmToken
import com.cooffee.member.domain.Member
import com.cooffee.member.domain.RefreshToken
import com.cooffee.member.enums.MemberType
import com.cooffee.member.exception.CustomException
import com.cooffee.member.exception.ExceptionType
import com.cooffee.member.model.SignInModel
import com.cooffee.member.model.SignInResponse
import com.cooffee.member.model.SignUpModel
import com.cooffee.member.repository.MemberRepository
import com.cooffee.member.repository.redis.ConfirmTokenRepository
import com.cooffee.member.repository.redis.RefreshTokenRepository
import com.cooffee.member.util.MailUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.logging.log4j.LogManager
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

private val log = LogManager.getLogger()

@Service
@Transactional(readOnly = true)
class MemberServiceImpl(
    private val memberRepository: MemberRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val confirmTokenRepository: ConfirmTokenRepository,
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
            confirm = false
        )

        val randomToken = UUID.randomUUID().toString()

        val confirmToken = ConfirmToken(
            email = member.email,
            token = randomToken
        )
        confirmTokenRepository.save(confirmToken)

        CoroutineScope(Dispatchers.IO).launch {
            mailUtil.sendMail(signUpModel.email, randomToken)
        }

        return memberRepository.save(member)
    }

    @Transactional
    override fun signIn(signInModel: SignInModel): SignInResponse = with(signInModel) {

        val member = memberRepository.findByEmail(email) ?: throw CustomException(ExceptionType.MEMBER_NOT_FOUND)
        if (member.confirm.not()) {
            throw CustomException(ExceptionType.MEMBER_NOT_CONFIRM)
        }

        encoder.matches(signInModel.password, member.password).takeIf { it }
            ?.let {
                val claim = JwtClaim(
                    id = member.id ?: throw CustomException(ExceptionType.MEMBER_ID_NOT_FOUND),
                    email = member.email,
                    name = member.name,
                )
                val accessToken = jwtUtil.createAccessToken(claim, jwtProperties)
                val refreshToken = jwtUtil.createRefreshToken(claim, jwtProperties)

                // Redis 에 토큰 저장
                val saveRefreshToken =
                    refreshTokenRepository.save(RefreshToken(member.id.toString(), refreshToken, accessToken))

                log.info("Member {} save refresh token : {}", saveRefreshToken)

                SignInResponse(accessToken = accessToken, refreshToken = refreshToken)

            } ?: throw CustomException(ExceptionType.INCORRECT_PASSWORD)
    }

    override fun getMemberByEmail(email: String): Member =
        memberRepository.findByEmail(email) ?: throw CustomException(ExceptionType.MEMBER_NOT_FOUND)


    override fun findAllMember(): List<Member> = memberRepository.findAll()

    @Transactional
    override fun confirmMember(email: String, token: String): String {
        val member: Member = getMemberByEmail(email)
        val findConfirmToken = confirmTokenRepository.findById(email)
        if (token == findConfirmToken.get().token) {
            member.activateMember()
            log.info("token match email : $email")
            return email
        } else {
            log.error("token not match email : $email")
            throw CustomException(ExceptionType.TOKEN_NOT_MATCH)
        }
    }
}