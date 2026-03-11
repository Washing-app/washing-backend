package com.washingapp.washing_backend.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "complaints")
data class Complaint(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne
    @JoinColumn(name = "machine_id", nullable = false)
    val machine: Machine,

    @Column(nullable = false)
    val description: String,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now()
)