package com.cooffee.member.exception

import org.apache.logging.log4j.LogManager
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

private val log = LogManager.getLogger()

@RestControllerAdvice
class CustomExceptionHandler {

    @ExceptionHandler(CustomException::class)
    fun exceptionHandler(e: CustomException): ResponseEntity<ExceptionResponse> {
        val exceptionType = e.exceptionType
        log.error("Exception Occurred : {}", exceptionType)
        return ExceptionResponse.toResponseEntity(exceptionType)
    }
}