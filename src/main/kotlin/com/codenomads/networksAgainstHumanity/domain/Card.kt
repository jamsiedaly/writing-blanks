package com.codenomads.networksAgainstHumanity.domain

import javax.persistence.*

@Entity
data class Card(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long?,
        @Column
        val text: String,
        @OneToOne
        val owner: Player
)