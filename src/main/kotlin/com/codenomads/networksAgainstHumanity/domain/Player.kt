package com.codenomads.networksAgainstHumanity.domain

import javax.persistence.*

@Entity
data class Player(
        @Id
        @Column(nullable = false)
        @GeneratedValue(strategy=GenerationType.IDENTITY)
        val token: Long?,
        @Column(nullable = false)
        val name: String,
        @Column
        val gameId: Long?,
        @Column
        var score: Int = 0,
        @Column
        var votes: Int = 0,
        @Column
        var voted: Boolean = false
)