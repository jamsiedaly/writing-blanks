package com.codenomads.networksAgainstHumanity.domain

import javax.persistence.*

@Entity
data class Round(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long?,
        @Column
        val number: Long,
        @Column
        val roundState: RoundState = RoundState.SUBMISSION,
        @OneToMany(targetEntity= Card::class, fetch = FetchType.EAGER)
        val players: List<Player>
)

enum class RoundState(val state: Int) {
    SUBMISSION(0),
    JUDGING(1),
    DONE(2)
}