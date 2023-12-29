package com.cooffee.member.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")
data class JwtProperties(
    val issuer: String,
    val subject: String,
    val expiresTime: Long,
    val secret: String,
)