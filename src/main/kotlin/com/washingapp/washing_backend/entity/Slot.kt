package com.washingapp.washing_backend.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "slots")
data class Slot(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "machine_id", nullable = false)
    val machine: Machine,

    @Column(name = "start_time", nullable = false)
    val startTime: LocalDateTime,

    @Column(name = "end_time", nullable = false)
    val endTime: LocalDateTime
)