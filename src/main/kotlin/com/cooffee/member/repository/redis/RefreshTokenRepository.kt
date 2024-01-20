package com.cooffee.member.repository.redis

import com.cooffee.member.domain.RefreshToken
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RefreshTokenRepository : CrudRepository<RefreshToken, String> {
    fun findByAccessToken(accessToken: String): RefreshToken?
    fun deleteByAccessToken(accessToken: String)
}