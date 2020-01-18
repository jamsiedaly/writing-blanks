package com.codenomads.networksAgainstHumanity.service

import com.codenomads.networksAgainstHumanity.domain.Game
import com.codenomads.networksAgainstHumanity.domain.Player
import com.codenomads.networksAgainstHumanity.domain.Round
import com.codenomads.networksAgainstHumanity.repository.GameRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GameService(
        @Autowired val gameRepository: GameRepository,
        @Autowired val playerService: PlayerService
) {

    fun createGame(name: String, player: Player): Game {
        var game = Game(null, name, mutableListOf(player))
        game = gameRepository.save(game)
        playerService.setPlayersGame(player, game)
        return game
    }

    fun findGameByName(gameName: String): Game {
        return gameRepository.findByName(gameName)
    }

    fun addPlayerToGame(player: Player, game: Game) {
        val players = game.players + player
        val updatedGame = game.copy(players = players)
        gameRepository.save(updatedGame)
        playerService.setPlayersGame(player, updatedGame)
    }

    fun getPlayersGame(player: Player): Game? = gameRepository.findAll().find { game ->
        game.players.find {
            it.name == player.name
        } != null
    }

    fun quit(player: Player) {
        val game = getPlayersGame(player)
        if (game != null) {
            val players = game.players - player
            if (players.isEmpty()) {
                gameRepository.delete(game)
            } else {
                val updatedGame = game.copy(players = players)
                gameRepository.save(updatedGame)
            }
        }
        playerService.quit(player)
    }

    fun addRoundToGame(game: Game, round: Round) {
        val updatedRounds = game.rounds + round
        val updateGame = game.copy(rounds = updatedRounds)
        gameRepository.save(updateGame)
    }
}