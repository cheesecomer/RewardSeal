package com.cheesecomer.rewardseal.data.repository

import com.cheesecomer.rewardseal.data.source.database.dao.RewardSheetDao
import com.cheesecomer.rewardseal.data.source.database.mapper.toEntity
import com.cheesecomer.rewardseal.data.source.database.mapper.toModel
import com.cheesecomer.rewardseal.model.RewardSheet

class RewardSheetRepository(
    private val dao: RewardSheetDao,
) {

    suspend fun findById(id: Long): RewardSheet? {
        return dao.findById(id)
            ?.toModel()
    }

    suspend fun save(sheet: RewardSheet) {
        val entity = sheet.toEntity()

        if (sheet.id == 0L) {
            dao.insert(entity)
        } else {
            dao.update(entity)
        }
    }

    suspend fun findActive(): List<RewardSheet> {
        return dao.findActive()
            .map { it.toModel() }
    }

    suspend fun increment(id: Long) : Boolean {
        val sheet = dao.findById(id)
        if (sheet == null) {
            return false
        }

        if (sheet.currentCount >= sheet.goalCount) {
            return false
        }

        dao.update(sheet.copy(
            currentCount = sheet.currentCount + 1
        ))

        return true
    }

    suspend fun restart(id: Long) {
        val sheet = dao.findById(id)
        if (sheet == null) {
            return
        }

        dao.update(sheet.copy(currentCount = 0))
    }

    suspend fun delete(id: Long) {
        val sheet = dao.findById(id)
        if (sheet == null) {
            return
        }

        dao.delete(sheet)
    }
}