package com.codenomads.networksAgainstHumanity.controller

import com.codenomads.networksAgainstHumanity.domain.Player
import com.codenomads.networksAgainstHumanity.service.PlayerService
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("player")
@CrossOrigin(origins = ["http://localhost:4200"])
class PlayerController(@Autowired val playerService: PlayerService) {

    @PostMapping("/")
    fun createPlayer(@RequestBody name: String): Long? {
        println(name)
        return playerService.createNewPlayer(name)
    }

    @GetMapping("/")
    fun getPlayerInfo(@RequestHeader("token") token: Long): PlayerDto {
        val player = playerService.getPlayer(token)
        return PlayerDto(player.name)
    }

    @GetMapping("/all")
    fun getAllPlayers(@RequestHeader("token") token: Long): List<PlayerDto> {
        val player = playerService.getPlayer(token)
        val players = playerService.getAllPlayersInGame(player)
        return players.map {
            player -> PlayerDto(player.name)
        }
    }
}

data class PlayerDto(val name: String) {
    override fun equals(other: Any?): Boolean {
        return if(other !is PlayerDto) false
        else other.name.equals(this.name)
    }
}
