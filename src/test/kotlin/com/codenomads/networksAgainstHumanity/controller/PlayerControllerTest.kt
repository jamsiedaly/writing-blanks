package com.codenomads.networksAgainstHumanity.controller


import com.codenomads.networksAgainstHumanity.domain.Game
import com.codenomads.networksAgainstHumanity.domain.Player
import com.codenomads.networksAgainstHumanity.repository.GameRepository
import com.codenomads.networksAgainstHumanity.repository.PlayerRepository
import com.google.gson.Gson
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
internal class PlayerControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var playerRepository: PlayerRepository

    @Autowired
    private lateinit var gameRepository: GameRepository

    private val gson = Gson()

    @Test
    fun createPlayer() {
        val playerName = "player1"
        val result = this.mockMvc.perform(post("/player/")
                .content(playerName))
                .andExpect(status().isOk)
                .andReturn()

        val token = result.response.contentAsString
        assertTrue(token.isNotBlank())
    }

    @Test
    fun getPlayerInfo() {
        val playerName = "player1"
        val playerToken = null
        var player = Player(playerToken, playerName, null)
        player = playerRepository.saveAndFlush(player)

        val result = this.mockMvc.perform(get("/player/")
                .header("token", player.token))
                .andExpect(status().isOk)
                .andReturn()

        val returnedPlayer: PlayerDto = gson.fromJson(result.response.contentAsString, PlayerDto::class.java)
        assertEquals(playerName, returnedPlayer.name)
    }

    @Test
    fun getAllPlayers() {
        val playerName = "player1"
        val player = Player(1, playerName, null)
        val playerName2 = "player2"
        val player2 = Player(2, playerName2, null)
        val players = listOf(player, player2)
        playerRepository.saveAll(players)
        val game = Game(1, "game", mutableListOf(player, player2))
        gameRepository.save(game)

        val result = this.mockMvc.perform(get("/player/all")
                .header("token", 1))
                .andExpect(status().isOk)
                .andReturn()

        val json = result.response.contentAsString
        val playerDtos : List<*> = gson.fromJson(json, Array<PlayerDto>::class.java).toList()
        assert(playerDtos.contains(PlayerDto(player.name)))
        assert(playerDtos.contains(PlayerDto(player2.name)))
    }
}