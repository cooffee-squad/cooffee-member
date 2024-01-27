package com.cooffee.member.domain

import com.cooffee.member.enums.MemberType
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "member")
class Member(
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
