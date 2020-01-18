package com.codenomads.networksAgainstHumanity.repository

import com.codenomads.networksAgainstHumanity.domain.Round
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoundRepository : JpaRepository<Round, Long>