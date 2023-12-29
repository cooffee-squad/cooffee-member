package com.cooffee.member.domain

import com.cooffee.member.enums.MemberType
import jakarta.persistence.*

@Entity
@Table(name = "member")
class Member (

    var name: String,

    var email: String,

    var password: String,

    var phone: String? = null,

    @Enumerated(EnumType.STRING)
    var type: MemberType,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    ) : BaseEntity()