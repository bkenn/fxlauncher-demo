package com.github.bkenn.demoserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class DemoserverApplication

fun main(args: Array<String>) {
    runApplication<DemoserverApplication>(*args)
}
