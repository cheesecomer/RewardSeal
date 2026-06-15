package com.cheesecomer.rewardseal.ui.screen.sheetdetail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.cheesecomer.rewardseal.data.repository.RewardSheetRepository
import com.cheesecomer.rewardseal.data.repository.CompletedRewardSheetRepository
import com.cheesecomer.rewardseal.model.CompletedRewardSheet
import com.cheesecomer.rewardseal.model.RewardSheet
import com.cheesecomer.rewardseal.ui.screen.sheetlist.SheetListViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class SheetDetailViewModel(
    private val rewardSheetRepository: RewardSheetRepository,
    private val completedRewardSheetRepository: CompletedRewardSheetRepository
) : ViewModel() {
    companion object {
        fun factory(
            rewardSheetRepository: RewardSheetRepository,
            completedRewardSheetRepository: CompletedRewardSheetRepository
        ): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    SheetDetailViewModel(
                        rewardSheetRepository,
                        completedRewardSheetRepository
                    )
                }
            }
    }

    var uiState by mutableStateOf(
        SheetDetailUiState()
    )
        private set

    fun load(sheetId: Long) {
        viewModelScope.launch {
            uiState = uiState.copy(
                sheet = rewardSheetRepository.findById(sheetId)
            )
        }
    }

    fun increment(sheetId: Long) {
        viewModelScope.launch {
            if (!rewardSheetRepository.increment(sheetId)) {
                return@launch
            }

            val sheet = rewardSheetRepository.findById(sheetId)
            uiState = uiState.copy(sheet = sheet)
            sheet
                ?.takeIf { it.currentCount == it.goalCount }
                ?.let(::createCompletedRewardSheet)
        }
    }

    fun createCompletedRewardSheet(sheet: RewardSheet) {
        viewModelScope.launch {
            completedRewardSheetRepository.save(
                CompletedRewardSheet(
                    id = 0,
                    sheetId = sheet.id,
                    title = sheet.title,
                    reward = sheet.reward,
                    goalCount = sheet.goalCount,
                    completedAt = LocalDateTime.now(),
                    rewardReceived = false
                )
            )
        }
    }

    fun delete(sheetId: Long) {
        viewModelScope.launch {
            rewardSheetRepository.delete(sheetId)
        }
    }

    fun restart(sheetId: Long) {
        viewModelScope.launch {
            rewardSheetRepository.restart(sheetId)
        }
        load(sheetId)
    }
}