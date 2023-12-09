package com.cooffee.member.domain

import com.cooffee.member.enums.MemberType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table


@Entity
@Table(name = "member")
class Member (

    @Id
    @Column
    val id: Long,

    @Column
    var name: String,

    @Column
    var email: String,

    @Column
    var password: String,

    @Column
    var phone: String? = null,

    @Column
    var type: MemberType,

    ) : BaseEntity()