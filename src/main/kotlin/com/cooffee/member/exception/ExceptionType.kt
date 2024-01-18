package com.cooffee.member.exception

import org.springframework.http.HttpStatus

enum class ExceptionType(val status: HttpStatus, val errorCode: String, val message: String) {

    ERROR_CODE_NOT_FOUND(HttpStatus.NOT_FOUND, "ER1000", "에러 코드가 없습니다."),

    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AU1000", "권한이 없습니다."),

    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "M1000", "회원을 찾을 수 없습니다."),
    ALREADY_EXISTED_MEMBER(HttpStatus.CONFLICT, "M1001", "이미 존재하는 회원입니다."),
    MEMBER_ID_NOT_FOUND(HttpStatus.NOT_FOUND, "M1002", "회원 아이디가 없습니다."),
    INCORRECT_PASSWORD(HttpStatus.BAD_REQUEST, "M1003", "패스워드가 일치하지 않습니다."),
    SEND_MAIL_FAIL(HttpStatus.BAD_REQUEST, "M1004", "메일 전송에 실패했습니다."),

    INVALID_TOKEN_ERROR(HttpStatus.BAD_REQUEST, "T1000", "유효하지 않은 토큰입니다.");

    companion object {
        fun exceptionType(errorCode: String): ExceptionType {
            return ExceptionType.values().firstOrNull { it.errorCode == errorCode } ?: ERROR_CODE_NOT_FOUND
        }
    }

}