package com.cooffee.member.exception

import org.springframework.http.ResponseEntity

class ExceptionResponse(val statusCode: Int, val status: String, val errorCode: String, val message: String) {
    companion object {
        fun toResponseEntity(exceptionType: ExceptionType): ResponseEntity<ExceptionResponse> {
            return ResponseEntity
                .status(exceptionType.status)
                .body(
                    ExceptionResponse(
                        exceptionType.status.value(),
                        exceptionType.name,
                        exceptionType.errorCode,
                        exceptionType.message,
                    )
                )
        }
    }
}