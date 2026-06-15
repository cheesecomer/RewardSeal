package com.cheesecomer.rewardseal.model

import java.time.LocalDateTime

data class CompletedRewardSheet(
    val id: Long,
    val sheetId: Long,
    val title: String,
    val reward: String,
    val goalCount: Int,
    val completedAt: LocalDateTime,
    val rewardReceived: Boolean,
)