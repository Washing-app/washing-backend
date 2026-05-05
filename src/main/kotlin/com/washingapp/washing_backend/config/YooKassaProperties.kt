package com.washingapp.washing_backend.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "yookassa")
data class YooKassaProperties(
    val shopId: String,
    val secretKey: String,
    val returnUrl: String
)