package com.cheesecomer.rewardseal.model

data class RewardSheet(
    val id: Long,
    val title: String,
    val reward: String,
    val currentCount: Int,
    val goalCount: Int) {
    val isCompleted: Boolean
        get() = currentCount >= goalCount
}