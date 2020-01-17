package com.codenomads.networksAgainstHumanity.repository

import com.codenomads.networksAgainstHumanity.domain.Game
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GameRepository: JpaRepository<Game, Long> {
    fun findByName(name: String): Game

}
