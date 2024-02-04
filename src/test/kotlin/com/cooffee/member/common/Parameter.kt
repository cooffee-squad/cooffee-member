package com.cooffee.member.common

import org.springframework.restdocs.request.ParameterDescriptor

open class Parameter(
    val descriptor: ParameterDescriptor,
) {
    val isOptional: Boolean = descriptor.isOptional

    open infix fun isOptional(value: Boolean): Parameter {
        if (value) descriptor.optional()
        return this
    }
}
