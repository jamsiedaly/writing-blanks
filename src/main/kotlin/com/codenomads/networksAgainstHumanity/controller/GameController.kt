package com.codenomads.networksAgainstHumanity.controller

import com.codenomads.networksAgainstHumanity.domain.Game
import com.codenomads.networksAgainstHumanity.service.GameService
import com.codenomads.networksAgainstHumanity.service.PlayerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/game")
class GameController(
        @Autowired val gameService: GameService,
        @Autowired val playerService: PlayerService
) {

    @PostMapping("/")
    fun createGame(@RequestBody gameName: String, @RequestHeader("token") playerToken: Long): Game {
        val player = playerService.getPlayer(playerToken)
        return gameService.createGame(gameName, player)
    }

    @PutMapping("/{gameName}")
    fun joinGame(@PathVariable gameName: String, @RequestHeader("token") playerToken: Long): Game {
        val game = gameService.findGameByName(gameName)
        val player = playerService.getPlayer(playerToken)
        gameService.addPlayerToGame(player, game)
        return game
    }

    @DeleteMapping("/")
    fun quitGame(@RequestHeader("token") playerToken: Long) {
        val player = playerService.getPlayer(playerToken)
        gameService.quit(player)
    }
}