package com.codenomads.networksAgainstHumanity.controller

import com.codenomads.networksAgainstHumanity.domain.Game
import com.codenomads.networksAgainstHumanity.domain.Player
import com.codenomads.networksAgainstHumanity.repository.GameRepository
import com.codenomads.networksAgainstHumanity.repository.PlayerRepository
import com.google.gson.Gson
import org.hibernate.Hibernate
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@AutoConfigureMockMvc
internal class GameControllerTest {

    private val gson = Gson()

    @Autowired
    private lateinit var gameRepository: GameRepository

    @Autowired
    private lateinit var playerRepository: PlayerRepository

    @Autowired
    private lateinit var mockMvc: MockMvc

    val hostName = "host"
    var host = Player(null, hostName, null)
    val playerName = "player"
    var player = Player(null, playerName, null)
    val gameName = "gameName1"

    @BeforeEach
    fun setup() {
        host = playerRepository.saveAndFlush(host)
        player = playerRepository.saveAndFlush(player)
    }

    @Test
    fun createGame() {
        val result = this.mockMvc.perform(MockMvcRequestBuilders.post("/game/")
                .header("token", host.token)
                .content(gameName))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()

        val json = result.response.contentAsString
        val game : Game = gson.fromJson(json, Game::class.java)
        val present = game.players.find { player ->
            player.name == host.name && host.token == host.token
        }
        assertNotNull(present)
    }

    @Test
    fun joinGame() {
        createGame()
        val result = this.mockMvc.perform(MockMvcRequestBuilders.put("/game/$gameName")
                .header("token", player.token))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()

        val games = gameRepository.findAll()
        val playersGame = games.find { game ->
            game.players.find { player ->
                player.name ==this.player.name
            } != null
        }

        assertNotNull(playersGame)
        assertNotNull(playersGame?.players?.find { it.name == host.name})
    }

    @Test
    fun quitGame() {
        val game = Game(null, "game", mutableListOf(host, player))
        gameRepository.save(game)

        mockMvc.perform(MockMvcRequestBuilders.delete("/game/")
                .header("token", host.token)
                .content(gameName))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()

        val updatedGame = gameRepository.findAll().first()
        assertTrue(updatedGame.players.contains(player))
        assertFalse(updatedGame.players.contains(host))
    }

    @Test
    fun allQuittingKillsGame() {
        val game = Game(null, "game", mutableListOf(host, player))
        gameRepository.save(game)

        mockMvc.perform(MockMvcRequestBuilders.delete("/game/")
                .header("token", host.token)
                .content(gameName))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()
        mockMvc.perform(MockMvcRequestBuilders.delete("/game/")
                .header("token", player.token)
                .content(gameName))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()

        assertTrue(gameRepository.findAll().isEmpty())
    }

    @AfterEach
    fun wipeData() {
        gameRepository.deleteAll()
        playerRepository.deleteAll()
    }
}