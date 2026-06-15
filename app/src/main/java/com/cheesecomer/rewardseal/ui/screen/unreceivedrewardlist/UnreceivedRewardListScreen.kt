package com.cheesecomer.rewardseal.ui.screen.unreceivedrewardlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cheesecomer.rewardseal.RewardSealApplication

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnreceivedRewardListScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
) {
    val application =
        LocalContext.current.applicationContext as RewardSealApplication
    val viewModel: UnreceivedRewardListViewModel = viewModel(
        factory = UnreceivedRewardListViewModel.factory(
            application.completedRewardSheetRepository
        )
    )
    val rewards = viewModel.rewards

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text("未受領のごほうび")
            },
            navigationIcon = {
                IconButton(
                    onClick = onBackClick
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "戻る"
                    )
                }
            }
        )

        if (rewards.isEmpty()) {
            Text("未受領のごほうびはありません")
            return
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(rewards) { reward ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(
                            text = reward.title,
                            style = MaterialTheme.typography.titleLarge,
                        )

                        Text("ごほうび：${reward.reward}")

                        Button(
                            onClick = {
                                viewModel.receiveReward(reward.id)
                            },
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text("受け取った")
                        }
                    }
                }
            }
        }
    }
}