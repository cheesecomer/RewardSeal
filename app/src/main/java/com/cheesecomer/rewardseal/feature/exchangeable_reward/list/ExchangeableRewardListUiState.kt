package com.cheesecomer.rewardseal.feature.exchangeable_reward.list

import com.cheesecomer.rewardseal.model.RewardMilestone

data class ExchangeableRewardListItemState(
    val id: Long,
    val title: String,
    val unconsumedCompletedCount: Int,
    val exchangeableMilestones: List<RewardMilestone>,
    val nextMilestone: RewardMilestone?
)

data class ExchangeableRewardListUiState(
    val sheets: List<ExchangeableRewardListItemState> = emptyList()
)