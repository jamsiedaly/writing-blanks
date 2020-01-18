package com.codenomads.networksAgainstHumanity.controller

import com.codenomads.networksAgainstHumanity.service.GameService
import com.codenomads.networksAgainstHumanity.service.RoundService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import kotlin.math.round

@RestController
@RequestMapping("/game/{gameName}/round")
class RoundController(@Autowired val roundService: RoundService, @Autowired val gameService: GameService) {

    @PostMapping
    fun startRound(@PathVariable("gameName") gameName: String): Long {
        val game = gameService.findGameByName(gameName)
        val roundNumber = (game.rounds.size + 1).toLong()
        val round = roundService.createRound(roundNumber, game.players)
        gameService.addRoundToGame(game, round)
        return round.id!!
    }
}