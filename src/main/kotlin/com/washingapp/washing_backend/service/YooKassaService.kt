package com.washingapp.washing_backend.service

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.washingapp.washing_backend.config.YooKassaProperties
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets
import java.util.Base64
import java.util.UUID

@Service
class YooKassaService(
    private val properties: YooKassaProperties
) {

    private val httpClient = HttpClient.newBuilder().build()
    private val objectMapper = jacksonObjectMapper()

    fun createPayment(
        amount: BigDecimal,
        description: String,
        bookingId: UUID
    ): YooKassaCreatePaymentResult {
        val idempotenceKey = UUID.randomUUID().toString()

        val requestBody = YooKassaCreatePaymentRequest(
            amount = AmountRequest(
                value = amount.setScale(2).toPlainString(),
                currency = "RUB"
            ),
            capture = true,
            confirmation = ConfirmationRequest(
                type = "redirect",
                returnUrl = properties.returnUrl
            ),
            description = description.take(128),
            metadata = mapOf("bookingId" to bookingId.toString())
        )

        val rawCredentials = "${properties.shopId}:${properties.secretKey}"
        val basicAuth = "Basic " + Base64.getEncoder()
            .encodeToString(rawCredentials.toByteArray(StandardCharsets.UTF_8))

        val requestJson = objectMapper.writeValueAsString(requestBody)

        val request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.yookassa.ru/v3/payments"))
            .header("Authorization", basicAuth)
            .header("Idempotence-Key", idempotenceKey)
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(requestJson))
            .build()

        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

        if (response.statusCode() !in 200..299) {
            throw RuntimeException(
                "YooKassa create payment failed: ${response.statusCode()} ${response.body()}"
            )
        }

        val parsed: YooKassaPaymentResponse = objectMapper.readValue(response.body())

        val confirmationUrl = parsed.confirmation?.confirmationUrl
            ?: throw RuntimeException("YooKassa did not return confirmation_url")

        return YooKassaCreatePaymentResult(
            paymentId = parsed.id,
            status = parsed.status,
            confirmationUrl = confirmationUrl,
            idempotenceKey = idempotenceKey
        )
    }
}

data class YooKassaCreatePaymentResult(
    val paymentId: String,
    val status: String,
    val confirmationUrl: String,
    val idempotenceKey: String
)

data class YooKassaCreatePaymentRequest(
    val amount: AmountRequest,
    val capture: Boolean,
    val confirmation: ConfirmationRequest,
    val description: String,
    val metadata: Map<String, String>
)

data class AmountRequest(
    val value: String,
    val currency: String
)

data class ConfirmationRequest(
    val type: String,
    @JsonProperty("return_url")
    val returnUrl: String
)

data class YooKassaPaymentResponse(
    val id: String,
    val status: String,
    val confirmation: YooKassaConfirmationResponse?
)

data class YooKassaConfirmationResponse(
    val type: String?,
    @JsonProperty("confirmation_url")
    val confirmationUrl: String?
)