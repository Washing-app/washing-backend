package com.washingapp.washing_backend.entity

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "payments")
data class Payment(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @OneToOne
    @JoinColumn(name = "booking_id", nullable = false, unique = true)
    val booking: Booking,

    @Column(name = "provider_payment_id")
    var providerPaymentId: String?,

    @Column(nullable = false)
    var status: String,

    @Column(nullable = false)
    val amount: BigDecimal,

    @Column(name = "confirmation_url", length = 1000)
    var confirmationUrl: String? = null,

    @Column(name = "idempotence_key", length = 64)
    var idempotenceKey: String? = null
)