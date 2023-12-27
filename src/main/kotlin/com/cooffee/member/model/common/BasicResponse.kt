package com.cooffee.member.model.common

import org.springframework.http.HttpStatus

data class BasicResponse<T>(
    var statusCode: Int = 200,
    var status: HttpStatus = HttpStatus.OK,
    var data: T? = null
) {
    companion object {
        fun <T> toResponse(status: HttpStatus, data: T): BasicResponse<T> {
            return BasicResponse(status.value(), status, data)
        }
    }
}
