package com.cooffee.member.model

import com.cooffee.member.domain.Address
import com.cooffee.member.enums.MemberType
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class MemberDetails(
    val id: Long? = null,
    var name: String,
    var email: String,
    private var password: String,
    var phone: String,
    var address: Address,
    var type: MemberType,
    var confirm: Boolean,
) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(GrantedAuthority { type.name })
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return email
    }

    override fun isAccountNonExpired(): Boolean {
        return false
    }

    override fun isAccountNonLocked(): Boolean {
        return false
    }

    override fun isCredentialsNonExpired(): Boolean {
        return false
    }

    override fun isEnabled(): Boolean {
        return true
    }
}
