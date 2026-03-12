package com.washingapp.washing_backend.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "bookings")
data class Booking(

    @Id
    val id: UUID = UUID.randomUUID(),

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @OneToOne
    @JoinColumn(name = "slot_id", nullable = false, unique = true)
    val slot: Slot,

    @ManyToOne
    @JoinColumn(name = "wash_type_id", nullable = false)
    val washType: WashType,

    @Column(nullable = false)
    var status: String,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "cancelled_at")
    val cancelledAt: LocalDateTime? = null
)