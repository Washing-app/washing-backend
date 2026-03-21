package com.washingapp.washing_backend.entity

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "wash_types")
data class WashType(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val name: String,

    @Column(name = "duration_minutes", nullable = false)
    val durationMinutes: Int,

    @Column(nullable = false)
    val price: BigDecimal,

    @Column(nullable = false)
    val temperature: Int,

    @Column(name = "spin_speed", nullable = false)
    val spinSpeed: Int,

    @Column(columnDefinition = "TEXT")
    val description: String
)