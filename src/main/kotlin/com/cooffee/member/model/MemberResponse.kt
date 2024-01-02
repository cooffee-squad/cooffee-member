package com.cooffee.member.model

data class MemberResponse(
    val email: String,
    val name: String,
    val phone: String?,
    val mainAddress: String?,
    val subAddress: String?,
    val zipcode: Int?,
)
