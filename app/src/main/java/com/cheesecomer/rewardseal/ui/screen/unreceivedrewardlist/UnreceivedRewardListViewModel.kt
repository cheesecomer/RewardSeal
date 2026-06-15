package com.cheesecomer.rewardseal.ui.screen.unreceivedrewardlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.cheesecomer.rewardseal.data.repository.CompletedRewardSheetRepository
import com.cheesecomer.rewardseal.model.CompletedRewardSheet
import kotlinx.coroutines.launch

class UnreceivedRewardListViewModel(
    private val completedRewardSheetRepository: CompletedRewardSheetRepository
) : ViewModel() {
    companion object {
        fun factory(
            completedRewardSheetRepository: CompletedRewardSheetRepository
        ): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    UnreceivedRewardListViewModel(
                        completedRewardSheetRepository
                    )
                }
            }
    }

    var rewards by mutableStateOf<List<CompletedRewardSheet>>(emptyList())
        private set

    init {
        reload()
    }

    fun reload() {
        viewModelScope.launch {
            rewards = completedRewardSheetRepository.findUnreceived()
        }
    }

    fun receiveReward(id: Long) {
        viewModelScope.launch {
            completedRewardSheetRepository.markRewardReceived(id)
        }
        reload()
    }
}