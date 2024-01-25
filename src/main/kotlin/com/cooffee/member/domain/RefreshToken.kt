package com.cooffee.member.domain

import com.cooffee.member.exception.CustomException
import com.cooffee.member.exception.ExceptionType
import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.index.Indexed

@RedisHash(value = "jwtToken", timeToLive = 60*60*24) // ttl 1 day
data class RefreshToken(

    @Id
    val id: String,

    var refreshToken: String,

    @Indexed
    var accessToken: String
) {
    init {
        require(id.isNotBlank()) {
            throw CustomException(ExceptionType.MEMBER_ID_NOT_FOUND)
        }
        require(refreshToken.isNotBlank()) {
            throw CustomException(ExceptionType.REFRESH_TOKEN_IS_BLANK)
        }
        require(accessToken.isNotBlank()) {
            throw CustomException(ExceptionType.ACCESS_TOKEN_IS_BLANK)
        }
    }
}
