package com.cooffee.member.util

import com.cooffee.member.exception.CustomException
import com.cooffee.member.exception.ExceptionType
import jakarta.mail.MessagingException
import jakarta.mail.internet.MimeMessage
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Component
import kotlin.random.Random

const val senderMail = "kadunsj@gmail.com"
private val log = LogManager.getLogger()

@Component
class MailUtil(
    @Value("\${server.port}") val port: Int,
    private val mailSender: JavaMailSender,
) {

    private var number: Int = 0

    fun sendMail(email: String, token: String) {
        val message: MimeMessage = createMail(email, token)
        mailSender.send(message)

        log.info("Cooffee signup mail number : {}", number)
    }

    fun createMail(email: String, token: String): MimeMessage {
        createNumber()
        val message: MimeMessage = mailSender.createMimeMessage()

        try {
            message.setFrom(senderMail)
            message.setRecipients(MimeMessage.RecipientType.TO, email)
            message.subject = "Cooffee 회원가입 인증"
            val body: String = """
            <h3>아래 링크를 통해 가입을 완료하세요</h3>
            <h3>링크는 30분간 유효합니다</h3>
            <h2><a href='http://localhost:$port/v1/member/confirm-mail?email=$email&token=$token' target='_blenk'>이메일 인증 확인</a></h2>
            <br/>
            <h3>감사합니다.</h3>
        """.trimIndent()
            message.setText(body, "UTF-8", "html")
            // TODO 일단 localhost로 보내도록 테스트 환경을 구성
        } catch (e: MessagingException) {
            e.printStackTrace()
            throw CustomException(ExceptionType.MEMBER_NOT_FOUND)
        }
        return message
    }

    private fun createNumber() {
        number = Random.nextInt(from = 100_000, until = 1_000_000)
    }
}