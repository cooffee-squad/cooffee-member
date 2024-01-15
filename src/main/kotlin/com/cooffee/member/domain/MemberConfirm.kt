package com.cooffee.member.domain

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "member_confirm")
data class MemberConfirm(

    var email: String,

    var token: String,

    @Column(name = "expired_at")
    val expiredAt: Instant = Instant.now(),

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val member: Member,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
)
