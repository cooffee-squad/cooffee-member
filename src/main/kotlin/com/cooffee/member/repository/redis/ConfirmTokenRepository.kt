package com.cooffee.member.repository.redis

import com.cooffee.member.domain.ConfirmToken
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ConfirmTokenRepository : CrudRepository<ConfirmToken, String> {

}