package com.cheesecomer.rewardseal.feature.sheet.detail

import com.cheesecomer.rewardseal.model.RewardSheet
import com.cheesecomer.rewardseal.model.RewardStamp

data class SheetDetailUiState(
    val sheet: RewardSheet? = null,
    val stamps: List<RewardStamp> = emptyList(),
)
