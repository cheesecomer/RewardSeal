package com.cheesecomer.rewardseal.feature.completed_sheet.detail

import com.cheesecomer.rewardseal.model.CompletedRewardSheet
import com.cheesecomer.rewardseal.model.RewardStamp

data class CompletedSheetDetailUiState (
    val reward: CompletedRewardSheet? = null,
    val stamps: List<RewardStamp> = emptyList(),
)