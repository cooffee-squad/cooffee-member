package com.cooffee.member

import org.springframework.boot.fromApplication
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.with

@TestConfiguration(proxyBeanMethods = false)
class TestCooffeeMemberApplication

fun main(args: Array<String>) {
    fromApplication<CooffeeMemberApplication>().with(TestCooffeeMemberApplication::class).run(*args)
}
