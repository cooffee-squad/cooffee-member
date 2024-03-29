package com.cooffee.member

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan
@SpringBootApplication
class CooffeeMemberApplication

fun main(args: Array<String>) {
    runApplication<CooffeeMemberApplication>(*args)
}
