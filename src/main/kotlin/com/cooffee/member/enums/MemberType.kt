package com.cooffee.member.enums

enum class MemberType {
    ADMIN,
    NORMAL,
    BIZ, ;

    companion object {
        operator fun invoke(type: String) = valueOf(type.uppercase())
    }
}
