package com.cooffee.member.common

import org.springframework.restdocs.headers.HeaderDescriptor

open class Header(
    val descriptor: HeaderDescriptor,
) {
    val isOptional: Boolean = descriptor.isOptional

    open infix fun isOptional(value: Boolean): Header {
        if (value) descriptor.optional()
        return this
    }
}
