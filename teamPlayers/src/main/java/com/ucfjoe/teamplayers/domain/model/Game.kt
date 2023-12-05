package com.ucfjoe.teamplayers.domain.model

import java.time.LocalDateTime

data class Game(val id: Long = 0, val teamId: Long, val gameDateTime: LocalDateTime)
// TODO (Add a isCompleted:Boolean)