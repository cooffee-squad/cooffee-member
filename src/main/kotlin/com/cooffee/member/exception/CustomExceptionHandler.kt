package com.cooffee.member.exception

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class CustomExceptionHandler {

    @ExceptionHandler(CustomException::class)
    fun handleCustomException(e: CustomException): ResponseEntity<Map<String, Any>> {
        val errorDetail = mapOf(
            "status" to e.status,
            "errorCode" to e.errorCode,
            "message" to e.message
        )
        return ResponseEntity(errorDetail, e.status)
    }
}