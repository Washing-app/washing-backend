package com.washingapp.washing_backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class WashingBackendApplication

fun main(args: Array<String>) {
	runApplication<WashingBackendApplication>(*args)
}
