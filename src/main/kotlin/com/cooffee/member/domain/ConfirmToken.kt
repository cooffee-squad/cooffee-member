package com.cooffee.member.domain

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash

@RedisHash(value = "confirmToken", timeToLive = 60 * 30) // ttl 30 minutes
data class ConfirmToken(

    @Id
    val email: String,
    val token: String,
)
