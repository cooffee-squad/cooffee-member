package com.cooffee.member.domain

import jakarta.persistence.Embeddable

@Embeddable
class Address (
    var mainAddress: String,
    var subAddress: String,
    var zipcode: Int,
)