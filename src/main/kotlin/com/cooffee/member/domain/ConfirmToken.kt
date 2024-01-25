package com.cooffee.member.domain

import com.cooffee.member.exception.CustomException
import com.cooffee.member.exception.ExceptionType
import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash

@RedisHash(value = "confirmToken", timeToLive = 60 * 30) // ttl 30 minutes
data class ConfirmToken(

    @Id
    val email: String,
    val token: String,
) {
    init {
        require(email.isNotBlank()) {
            throw CustomException(ExceptionType.EMAIL_IS_BLANK)
        }
        require(token.isNotBlank()) {
            throw CustomException(ExceptionType.CREATE_TOKEN_FAIL)
        }
    }
}
