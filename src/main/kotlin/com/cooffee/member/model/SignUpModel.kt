package com.cooffee.member.model

data class SignUpModel(
    val email: String,
    val name: String,
    val password: String,
    val phone: String,
    val mainAddress: String,
    val subAddress: String,
    val zipcode: Int,
)
