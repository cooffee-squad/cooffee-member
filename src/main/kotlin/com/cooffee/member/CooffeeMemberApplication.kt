package com.cooffee.member

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan
@SpringBootApplication(exclude = [SecurityAutoConfiguration::class])
class CooffeeMemberApplication

fun main(args: Array<String>) {
    runApplication<CooffeeMemberApplication>(*args)
}
