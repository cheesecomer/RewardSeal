package com.cheesecomer.rewardseal.feature.sheet.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cheesecomer.rewardseal.RewardSealApplication
import com.cheesecomer.rewardseal.model.RewardSheet
import com.cheesecomer.rewardseal.model.RewardStamp
import com.cheesecomer.rewardseal.model.StampType
import com.cheesecomer.rewardseal.ui.component.RewardBoardState
import com.cheesecomer.rewardseal.ui.component.RewardBoardView
import com.cheesecomer.rewardseal.ui.component.dialog.DeleteSheetDialog
import com.cheesecomer.rewardseal.ui.component.dialog.SelectStampDialog

@Composable
private fun CompletedSheetActions(
    sheet: RewardSheet,
    onRestartClick: () -> Unit,
    onRestartWithEditClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "\uD83C\uDF89 シートが満タンになりました！",
            style = MaterialTheme.typography.headlineMedium,
        )

        Text("${sheet.title} を ${sheet.goalCount}回 がんばってシートを満タンにしました。")

        Text(
            text = "ごほうび交換画面で、ためたシートとごほうびを交換できます。",
            style = MaterialTheme.typography.bodyMedium,
        )

        Button(
            onClick = onRestartClick,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("もっとがんばる")
        }

        Button(
            onClick = onRestartWithEditClick,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("ごほうびや回数を変えてがんばる")
        }
    }
}

private enum class SheetDetailDialog {
    Stamp,
    Delete,
}

@Composable
private fun SheetDetailDialogs(
    activeDialog: SheetDetailDialog?,
    onDismissRequest: () -> Unit,
    onDeleteRequest: () -> Unit,
    onStampTypeSelected: (StampType) -> Unit,
) {
    when (activeDialog) {
        SheetDetailDialog.Delete -> {
            DeleteSheetDialog(
                onDeleteRequest = onDeleteRequest,
                onDismissRequest = onDismissRequest,
            )
        }

        SheetDetailDialog.Stamp -> {
            SelectStampDialog(
                onDismissRequest = onDismissRequest,
                onStampTypeSelected = onStampTypeSelected,
            )
        }

        null -> Unit
    }
}

@Composable
private fun ProgressSheet(
    sheet: RewardSheet,
    stamps: List<RewardStamp>,
    onEditRequest: () -> Unit,
    onDeleteRequest: () -> Unit,
    onStampTypeSelected: (StampType) -> Unit,
) {
    var activeDialog by remember { mutableStateOf<SheetDetailDialog?>(null) }


    SheetDetailDialogs(
        activeDialog = activeDialog,
        onDismissRequest = {
            activeDialog = null
        },
        onDeleteRequest = {
            activeDialog = null
            onDeleteRequest()
        },
        onStampTypeSelected = { stampType ->
            activeDialog = null
            onStampTypeSelected(stampType)
        },
    )

    Column {
        Text("${sheet.currentCount} / ${sheet.goalCount}")
        Button(
            onClick = {
                activeDialog = SheetDetailDialog.Stamp
            },
            enabled = sheet.currentCount < sheet.goalCount,
        ) {
            Text("スタンプを押す")
        }

        Button(
            onClick = onEditRequest
        ) {
            Text("編集")
        }

        Button(
            onClick = {
                activeDialog = SheetDetailDialog.Delete
            }
        ) {
            Text("削除")
        }

        RewardBoardView(
            board = RewardBoardState(
                title = sheet.title,
                currentCount = sheet.currentCount,
                goalCount = sheet.goalCount,
            ),
            stamps = stamps,
            modifier = Modifier.weight(1f)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SheetDetailScreen(
    sheetId: Long,
    onBackClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onRestartWithEditClick: (sheetId: Long) -> Unit,
) {
    val application =
        LocalContext.current.applicationContext as RewardSealApplication
    val viewModel: SheetDetailViewModel = viewModel(
        factory = SheetDetailViewModel.factory(
            application.rewardSheetRepository,
            application.completedRewardSheetRepository,
            application.rewardStampRepository
        )
    )

    LaunchedEffect(sheetId) {
        viewModel.load(sheetId)
    }

    val uiState = viewModel.uiState
    val sheet = uiState.sheet
    if (sheet == null) {
        Text("見つかりません")
        return
    }

    Column {
        CenterAlignedTopAppBar(
            title = {
                Text(sheet.title)
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
        if (sheet.isCompleted) {
            CompletedSheetActions(
                sheet = sheet,
                onRestartClick = {
                    viewModel.restart(sheetId)
                },
                onRestartWithEditClick = {
                    onRestartWithEditClick(sheetId)
                },
            )
        } else {
            ProgressSheet(
                sheet = sheet,
                stamps = uiState.stamps,
                onEditRequest = onEditClick,
                onDeleteRequest = {
                    viewModel.delete(sheetId) {
                        onDeleteClick()
                    }
                },
                onStampTypeSelected = { stampType ->
                    viewModel.increment(
                        sheetId = sheetId,
                        stampType = stampType,
                    )
                },
            )
        }
    }
}
