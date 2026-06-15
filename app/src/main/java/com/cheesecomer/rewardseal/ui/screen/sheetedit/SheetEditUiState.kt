package com.cheesecomer.rewardseal.ui.screen.sheetedit

data class SheetEditUiState(
    val sheetId: Long = 0,
    val title: String = "",
    val reward: String = "",
    val goalCount: Int = 10
)
