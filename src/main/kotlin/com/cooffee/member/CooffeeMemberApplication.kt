package com.cooffee.member

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [SecurityAutoConfiguration::class])
class CooffeeMemberApplication

fun main(args: Array<String>) {
    runApplication<CooffeeMemberApplication>(*args)
}
