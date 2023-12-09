package com.cooffee.member.enums

enum class MemberType {

    ADMIN, NORMAL;

    companion object {
        operator fun invoke(type: String) = valueOf(type.uppercase())
    }
}