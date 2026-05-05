package com.washingapp.washing_backend

import com.washingapp.washing_backend.config.YooKassaProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.boot.context.properties.EnableConfigurationProperties

@EnableScheduling
@SpringBootApplication
@EnableConfigurationProperties(YooKassaProperties::class)
class WashingBackendApplication

fun main(args: Array<String>) {
	runApplication<WashingBackendApplication>(*args)
}
