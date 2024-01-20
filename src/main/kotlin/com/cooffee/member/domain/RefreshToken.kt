package com.cooffee.member.domain

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
)
