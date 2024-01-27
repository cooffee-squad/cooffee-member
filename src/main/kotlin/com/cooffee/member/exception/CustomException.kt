package com.cooffee.member.exception

import org.springframework.web.server.ResponseStatusException

class CustomException(exceptionType: ExceptionType) : ResponseStatusException(exceptionType.status, exceptionType.message) {
    val errorCode = exceptionType.errorCode
    val status = exceptionType.status
}
