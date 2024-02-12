package com.cooffee.member.enums

enum class MemberType {
    ROLE_ADMIN,
    ROLE_NORMAL,
    ROLE_BIZ, ;

    companion object {
        operator fun invoke(type: String) = valueOf(type.uppercase())
    }
}
