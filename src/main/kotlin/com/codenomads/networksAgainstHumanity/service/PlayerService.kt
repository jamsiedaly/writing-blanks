package com.codenomads.networksAgainstHumanity.service

import com.codenomads.networksAgainstHumanity.domain.Game
import com.codenomads.networksAgainstHumanity.domain.Player
import com.codenomads.networksAgainstHumanity.repository.GameRepository
import com.codenomads.networksAgainstHumanity.repository.PlayerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PlayerService(@Autowired val playerRepository: PlayerRepository, @Autowired val gameRepository: GameRepository) {

    fun createNewPlayer(name: String): Long? {
        val newPlayer = Player(null, name, -1)
        playerRepository.save(newPlayer)
        return newPlayer.token
    }

    fun getPlayer(token: Long) : Player {
        return playerRepository.findByToken(token)
    }

    fun getAllPlayers(): List<Player> {
        return playerRepository.findAll()
    }

    fun setPlayersGame(player: Player, game: Game) {
        val updatedPlayer = Player(
                player.token,
                player.name,
                game.id)
        playerRepository.save(updatedPlayer)
    }

    fun quit(player: Player) {
        val updatedPlayer = Player(
                player.token,
                player.name,
                null,
                score = 0)
        playerRepository.save(updatedPlayer)
    }

    fun getAllPlayersInGame(player: Player): List<Player> {
        val game = getPlayersGame(player)
        return game?.players ?: mutableListOf()
    }

    fun getPlayersGame(player: Player): Game? {
        return gameRepository.findAll().find { game ->
            game.players.find {
                it.name == player.name
            } != null
        }
    }
}