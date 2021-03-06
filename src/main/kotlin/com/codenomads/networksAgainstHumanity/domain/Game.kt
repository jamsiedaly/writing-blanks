package com.codenomads.networksAgainstHumanity.domain

import javax.persistence.*

@Entity
data class Game(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    @Column
    val name: String,
    @OneToMany(targetEntity= Player::class, fetch = FetchType.EAGER)
    val players: List<Player>,
    @OneToMany(targetEntity= Round::class)
    val rounds: List<Round> = mutableListOf()
)