package com.cheesecomer.rewardseal.data.source.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cheesecomer.rewardseal.data.source.database.dao.CompletedRewardSheetDao
import com.cheesecomer.rewardseal.data.source.database.dao.RewardSheetDao
import com.cheesecomer.rewardseal.data.source.database.entity.CompletedRewardSheetEntity
import com.cheesecomer.rewardseal.data.source.database.entity.RewardSheetEntity

@Database(
    exportSchema = false,
    entities = [
        RewardSheetEntity::class,
        CompletedRewardSheetEntity::class,
    ],
    version = 1,
)
@TypeConverters(LocalDateTimeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun rewardSheetDao(): RewardSheetDao
    abstract fun completedRewardSheetDao(): CompletedRewardSheetDao
}