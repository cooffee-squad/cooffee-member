package com.cooffee.member.util

import com.cooffee.member.exception.CustomException
import com.cooffee.member.exception.ExceptionType
import jakarta.mail.MessagingException
import jakarta.mail.internet.MimeMessage
import org.apache.logging.log4j.LogManager
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Component
import kotlin.random.Random

const val senderMail = "kadunsj@gmail.com"
private val log = LogManager.getLogger()

@Component
class MailUtil(
    private val mailSender: JavaMailSender,
) {

    private var number: Int = 0

    fun sendMail(email: String) {
        val message: MimeMessage = createMail(email)
        mailSender.send(message)

        log.info("Cooffee signup mail number : {}", number)
    }

    fun createMail(email: String): MimeMessage {
        createNumber()
        val message: MimeMessage = mailSender.createMimeMessage()

        try {
            message.setFrom(senderMail)
            message.setRecipients(MimeMessage.RecipientType.TO, email)
            message.subject = "이메일 인증"
            val body: String = """
            <h3>요청하신 인증 번호입니다.</h3>
            <h1>$number</h1>
            <h3>감사합니다.</h3>
        """.trimIndent()
            message.setText(body, "UTF-8", "html")
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