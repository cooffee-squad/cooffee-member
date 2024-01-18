package com.cooffee.member.domain

import com.cooffee.member.enums.MemberType
import jakarta.persistence.*

@Entity
@Table(name = "member")
class Member (

    var name: String,

    var email: String,

    var password: String,

    var phone: String,

    @Embedded
    var address: Address,

    @Enumerated(EnumType.STRING)
    var type: MemberType,

    @Column(name = "confirm_flag")
    var confirm: Boolean,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    ) : BaseEntity() {
    fun activateMember() {
        this.confirm = true
    }
}