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

    @ManyToOne
    @JoinColumn(name = "slot_id", nullable = false)
    val startSlot: Slot,

    @ManyToOne
    @JoinColumn(name = "wash_type_id", nullable = false)
    val washType: WashType,

    @Column(nullable = false)
    var status: String,

    @Column(name = "start_time", nullable = false)
    val startTime: LocalDateTime,

    @Column(name = "end_time", nullable = false)
    val endTime: LocalDateTime,

    val createdAt: LocalDateTime = LocalDateTime.now(),

    var cancelledAt: LocalDateTime? = null
)