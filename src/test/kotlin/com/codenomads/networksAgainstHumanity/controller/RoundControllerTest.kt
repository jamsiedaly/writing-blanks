package com.codenomads.networksAgainstHumanity.controller

import com.codenomads.networksAgainstHumanity.domain.Game
import com.codenomads.networksAgainstHumanity.domain.Player
import com.codenomads.networksAgainstHumanity.repository.GameRepository
import com.codenomads.networksAgainstHumanity.repository.PlayerRepository
import com.codenomads.networksAgainstHumanity.repository.RoundRepository
import com.google.gson.Gson
import org.hibernate.Hibernate
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@AutoConfigureMockMvc
internal class RoundControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var playerRepository: PlayerRepository

    @Autowired
    private lateinit var gameRepository: GameRepository

    @Autowired
    private lateinit var roundRepository: RoundRepository

    val hostName = "host"
    var host = Player(null, hostName, null)
    val playerName = "player"
    var player = Player(null, playerName, null)
    val gameName = "gameName1"
    lateinit var game: Game
    val url = "/game/$gameName/round"
    private val gson = Gson()

    @BeforeEach
    fun setUp() {
        host = playerRepository.saveAndFlush(host)
        player = playerRepository.saveAndFlush(player)
        game = Game(null, gameName, mutableListOf(host, player))
        game = gameRepository.saveAndFlush(game)
    }

    @AfterEach
    fun tearDown() {
        playerRepository.deleteAll()
        gameRepository.deleteAll()
        roundRepository.deleteAll()
    }

    @Test
    @Transactional
    fun startRound() {
        val result = this.mockMvc.perform(MockMvcRequestBuilders.post(url)
                .header("token", host.token))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()

        val json = result.response.contentAsString
        val roundId = gson.fromJson(json, Long::class.java)
        val roundOptional = roundRepository.findById(roundId)
        val game = gameRepository.findByName(gameName)
        assert(roundOptional.isPresent)
        assert(game.rounds.contains(roundOptional.get()))
    }
}