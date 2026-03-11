package com.washingapp.washing_backend.entity

import jakarta.persistence.*

@Entity
@Table(name = "machines")
data class Machine(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val status: String,

    @Column(nullable = false)
    val location: String,
)