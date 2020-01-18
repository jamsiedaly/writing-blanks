package com.codenomads.networksAgainstHumanity.service

import com.codenomads.networksAgainstHumanity.domain.Player
import com.codenomads.networksAgainstHumanity.domain.Round
import com.codenomads.networksAgainstHumanity.repository.RoundRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RoundService(@Autowired val roundRepository: RoundRepository) {
    fun createRound(number: Long, players: List<Player>): Round {
        val playersInRound = players.toList()
        val round = Round(null, number, players = playersInRound)
        return roundRepository.save(round)
    }

}
