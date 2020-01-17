package com.codenomads.networksAgainstHumanity.repository

import com.codenomads.networksAgainstHumanity.domain.Player
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PlayerRepository : JpaRepository<Player, Long> {
    fun findByToken(token: Long): Player
}