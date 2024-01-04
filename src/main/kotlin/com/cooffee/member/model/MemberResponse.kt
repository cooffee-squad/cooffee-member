package com.cooffee.member.model

import com.cooffee.member.domain.Address

data class MemberResponse(
    val email: String,
    val name: String,
    val phone: String,
    val address: Address,
)
